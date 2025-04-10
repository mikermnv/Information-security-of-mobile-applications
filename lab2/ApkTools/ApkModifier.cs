namespace lab2.ApkTools;

using System.Diagnostics;
using System.IO.Compression;

public static class ApkModifier
{
    public static FileStream InjectSecret(string originalApkPath, string secretText)
    {
        string tempApkPath = Path.Combine(Path.GetTempPath(), $"{Guid.NewGuid()}.apk");
        File.Copy(originalApkPath, tempApkPath);

        using (FileStream zipToOpen = new FileStream(tempApkPath, FileMode.Open, FileAccess.ReadWrite))
        using (ZipArchive archive = new ZipArchive(zipToOpen, ZipArchiveMode.Update))
        {
            string entryName = "assets/secret.txt";

            var existingEntry = archive.GetEntry(entryName);
            existingEntry?.Delete();

            var entry = archive.CreateEntry(entryName);

            using (var writer = new StreamWriter(entry.Open()))
            {
                writer.Write(secretText);
            }
        }

        string projectRoot = Path.GetFullPath(Path.Combine(AppContext.BaseDirectory, "..", "..", ".."));
        string apksignerPath = Path.Combine(projectRoot, "ApkSigner", "build-tools", "36.0.0-rc5", "apksigner.bat");
        string keystorePath = Path.Combine(projectRoot, "ApkSigner", "build-tools", "36.0.0-rc5", "myapksigner.keystore");

        string signedApkPath = tempApkPath;

        string arguments = $"sign --ks \"{keystorePath}\" --ks-key-alias signerapp --ks-pass pass:qweasdzxc --key-pass pass:qweasdzxc \"{signedApkPath}\"";

        var processInfo = new ProcessStartInfo
        {
            FileName = apksignerPath,
            Arguments = arguments,
            RedirectStandardOutput = true,
            RedirectStandardError = true,
            UseShellExecute = false,
            CreateNoWindow = true
        };

        using (var process = Process.Start(processInfo))
        {
            process.WaitForExit();

            if (process.ExitCode != 0)
            {
                string error = process.StandardError.ReadToEnd();
                throw new Exception($"APK signing failed: {error}");
            }
        }

        return new FileStream(signedApkPath, FileMode.Open, FileAccess.Read, FileShare.Read, 4096, FileOptions.DeleteOnClose);
    }
}

