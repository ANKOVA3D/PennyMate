document.addEventListener('DOMContentLoaded', () => {
    const budgetValueElement = document.getElementById('budgetValue'); // The element where the budget will be shown
  
    // Assuming the user is authenticated and we can fetch their ID from the session or an API
    fetch("http://localhost:8080/users/me", {
      method: "GET",
      credentials: "include" // This sends the cookies/session along with the request
    })
      .then((res) => {
        if (!res.ok) {
          throw new Error("Non autenticato");
        }
        return res.json();
      })
      .then((data) => {
        // Assume the user data includes an ID (adjust if your API returns it differently)
        const userId = data.id; // or some other way of retrieving the user ID
  
        // Now, fetch the user's budget using the ID
        return fetch(`http://localhost:8080/users/${userId}/budget`, {
          method: "GET",
          credentials: "include", // Sends session cookies along with the request
        });
      })
      .then((res) => {
        if (!res.ok) {
          throw new Error("Errore nel recupero del budget");
        }
        return res.json();
      })
      .then((data) => {
        // Update the budget display element
        const weeklyBudget = data.weekly_budget;
        budgetValueElement.textContent = `${weeklyBudget.toFixed(2)} €`; // Display the budget with 2 decimal places
      })
      .catch((err) => {
        console.error("Impossibile caricare il budget:", err);
        budgetValueElement.textContent = "Errore nel recupero del budget";
      });
  });
  