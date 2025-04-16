using System.Diagnostics;
using lab2.ApkTools;
using lab2.Models;
using Microsoft.AspNetCore.Mvc;

namespace lab2.Controllers
{
    public class HomeController : Controller
    {
        [HttpGet]
        public IActionResult Index()
        {
            return View();
        }

        [HttpPost]
        public IActionResult Download([FromForm] string secret = "")
        {
            if (secret.Length > 10000)
            {
                ModelState.AddModelError("secret", "The secret must not exceed 10,000 characters.");
                return View("Index");
            }
            var originalApkPath = Path.Combine(Directory.GetCurrentDirectory(), "Apk", "original.apk");
            var stream = ApkModifier.InjectSecret(originalApkPath, secret);
            return File(stream, "application/vnd.android.package-archive", "Secret.apk");
        }

        [ResponseCache(Duration = 0, Location = ResponseCacheLocation.None, NoStore = true)]
        public IActionResult Error()
        {
            return View(new ErrorViewModel { RequestId = Activity.Current?.Id ?? HttpContext.TraceIdentifier });
        }
    }
}
