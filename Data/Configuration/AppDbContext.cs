using Microsoft.EntityFrameworkCore;
using P._Expense_Tracker.Data.Models;

namespace P._Expense_Tracker.Data.Configuration
{
        public class AppDbContext : DbContext
        {
            public AppDbContext(DbContextOptions<AppDbContext> options) : base(options)
            {
            }

            public DbSet<Expenses> Expenses { get; set; } = default!;
            public DbSet<AddNote> Notes { get; set; } = default!;
            public DbSet<AddFunds> Funds { get; set; } = default!;
        }
    
}
