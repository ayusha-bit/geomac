using Microsoft.AspNetCore.Components;
using Microsoft.AspNetCore.Components.Web;
using P._Expense_Tracker.Data;
using P._Expense_Tracker.Data.Configuration;
using Microsoft.EntityFrameworkCore;
using P._Expense_Tracker.Data.Services;
using Syncfusion.Blazor.Popups;
using Syncfusion.Blazor;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddRazorPages();
builder.Services.AddServerSideBlazor();
builder.Services.AddSingleton<WeatherForecastService>();
builder.Services.AddSyncfusionBlazor();
builder.Services.AddDbContext<AppDbContext>(options =>
{
    options.UseSqlite(builder.Configuration.GetConnectionString("DefaultConnection")
        ?? throw new InvalidOperationException("Default connection not found"));
});
builder.Services.AddScoped<IServiceInterface, ServiceImplementation>();
builder.Services.AddScoped<SfDialogService>();
var app = builder.Build();

// Configure the HTTP request pipeline.
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Error");
    // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
    app.UseHsts();
}

app.UseHttpsRedirection();

app.UseStaticFiles();

app.UseRouting();

app.MapBlazorHub();
app.MapFallbackToPage("/_Host");

app.Run();
