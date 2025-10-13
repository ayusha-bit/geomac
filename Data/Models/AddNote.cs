namespace P._Expense_Tracker.Data.Models
{
    public class AddNote
    {
        public int Id { get; set; }
        public string Description { get; set; } = string.Empty;
        public DateTime? DateAdded { get; set; }
    }
}
