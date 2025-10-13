using System.ComponentModel.DataAnnotations;

namespace P._Expense_Tracker.Data.Models
{
    public class AddFunds
    {
        public int Id { get; set; }

        [Required]
        public decimal Amount { get; set; }
    }
}
