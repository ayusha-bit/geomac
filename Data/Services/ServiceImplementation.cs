using Microsoft.EntityFrameworkCore;
using P._Expense_Tracker.Data.Configuration;
using P._Expense_Tracker.Data.Models;

namespace P._Expense_Tracker.Data.Services
{
    public class ServiceImplementation : IServiceInterface
    {
        private readonly AppDbContext conn;

        public ServiceImplementation(AppDbContext conn)
        {
            this.conn = conn ?? throw new ArgumentNullException(nameof(conn));
        }

        // Add or update expenses
        public async Task<(bool flag, string message)> AddOrUpdateExpensesAsync(Expenses expenses)
        {
            if (expenses is null)
                return (false, "Expenses object is null.");

            if (expenses.Id == 0)
            {
                conn.Expenses.Add(expenses);
            }
            else
            {
                var existingExpense = await conn.Expenses.FirstOrDefaultAsync(e => e.Id == expenses.Id);
                if (existingExpense is null)
                    return (false, "Expense not found.");

                existingExpense.Name = expenses.Name ?? existingExpense.Name;
                existingExpense.Amount = expenses.Amount;
                existingExpense.DateAdded = expenses.DateAdded;
            }

            await conn.SaveChangesAsync();
            return (true, expenses.Id == 0 ? "Saved" : "Updated");
        }

        // Get all expenses
        public async Task<List<Expenses>> GetAllExpensesAsync()
        {
            return await conn.Expenses.ToListAsync() ?? new();
        }

        // Delete expenses
        public async Task<bool> DeleteExpensesAsync(int id)
        {
            var expense = await conn.Expenses.FirstOrDefaultAsync(e => e.Id == id);
            if (expense is null)
                return false;

            conn.Expenses.Remove(expense);
            await conn.SaveChangesAsync();
            return true;
        }

        // Add funds
        public async Task<int> AddFundAsync(AddFunds fund)
        {
            if (fund is null)
                return -1;

            try
            {
                var existingFund = await conn.Funds.FirstOrDefaultAsync(f => f.Amount >= 0);
                if (existingFund is not null)
                {
                    var updatedAmount = existingFund.Amount + fund.Amount;
                    if (updatedAmount >= 0)
                    {
                        existingFund.Amount = updatedAmount;
                        await conn.SaveChangesAsync();
                        return 2; // Updated existing fund
                    }
                    return 3; // Insufficient fund
                }

                if (fund.Amount < 0)
                    return 4; // Negative fund addition not allowed

                conn.Funds.Add(fund);
                await conn.SaveChangesAsync();
                return 1; // New fund added
            }
            catch (Exception)
            {
                return -1; // Error occurred
            }
        }

        // Get available funds
        public async Task<decimal> GetAvailableFund()
        {
            return (await conn.Funds.ToListAsync())?.Sum(f => f.Amount) ?? 0;
        }

        // Add a note
        public async Task<(bool flag, string message)> AddNoteAsync(AddNote note)
        {
            if (note is null)
                return (false, "Note is null.");

            conn.Notes.Add(note);
            await conn.SaveChangesAsync();
            return (true, "Saved");
        }

        // Delete a note
        public async Task<(bool flag, string message)> DeleteNoteAsync(int id)
        {
            var note = await conn.Notes.FirstOrDefaultAsync(n => n.Id == id);
            if (note is null)
                return (false, "Note not found.");

            conn.Notes.Remove(note);
            await conn.SaveChangesAsync();
            return (true, "Deleted");
        }

        // Get all notes
        public async Task<List<AddNote>> GetNoteAsync()
        {
            return await conn.Notes.ToListAsync() ?? new();
        }
    }
}
