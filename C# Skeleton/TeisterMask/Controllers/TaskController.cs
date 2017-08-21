using System;
using System.Linq;
using System.Web.Mvc;
using TeisterMask.Models;

namespace TeisterMask.Controllers
{
        [ValidateInput(false)]
	public class TaskController : Controller
	{
	    [HttpGet]
            [Route("")]
	    public ActionResult Index()
	    {
            using (var db = new TeisterMaskDbContext())
            {
                var tasks = db.Tasks.ToList();
                return View(tasks);
            }
        }

        [HttpGet]
        [Route("create")]
        public ActionResult Create()
		{
            return View();
        }

		[HttpPost]
		[Route("create")]
        [ValidateAntiForgeryToken]
		public ActionResult Create(Task task)
		{

            if (ModelState.IsValid)
            {
                using (var db = new TeisterMaskDbContext())
                {
                    db.Tasks.Add(task);
                    db.SaveChanges();
                }

                return RedirectToAction("Index");
            }

            return View(task);
        }

		[HttpGet]
		[Route("edit/{id}")]
        public ActionResult Edit(int id)
		{
            using (var db = new TeisterMaskDbContext())
            {
                var taskToEdit = db.Tasks.Find(id);

                if (taskToEdit == null)
                {
                    return HttpNotFound();
                }

                return View(taskToEdit);
            }
        }

		[HttpPost]
		[Route("edit/{id}")]
        [ValidateAntiForgeryToken]
		public ActionResult EditConfirm(int id, Task taskModel)
		{
            if (ModelState.IsValid)
            {
                using (var db = new TeisterMaskDbContext())
                {
                    var taskFromDb = db.Tasks.Find(id);

                    if (taskFromDb == null)
                    {
                        return HttpNotFound();
                    }

                    taskFromDb.Title = taskModel.Title;
                    taskFromDb.Status = taskModel.Status;
                    db.SaveChanges();
                }
            }

            return RedirectToAction("Index");
        }
	}
}