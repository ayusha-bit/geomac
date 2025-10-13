using P._Expense_Tracker.Data.Models;

namespace P._Expense_Tracker.Data.Services
{
    public interface IServiceInterface
    {
        Task<(bool flag, string message)> AddOrUpdateExpensesAsync(Expenses expenses);
        Task<List<Expenses>> GetAllExpensesAsync();
        Task<bool> DeleteExpensesAsync(int id);

        // Fund
        Task<int> AddFundAsync(AddFunds fund);
        Task<decimal> GetAvailableFund();

        // Note
        Task<(bool flag, string message)> AddNoteAsync(AddNote note);
        Task<(bool flag, string message)> DeleteNoteAsync(int id);
        Task<List<AddNote>> GetNoteAsync();
    }
}
