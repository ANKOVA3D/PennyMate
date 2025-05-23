// script.js

// Mostra nome utente
fetch("http://localhost:8080/users/me", { credentials: "include" })
  .then(res => res.json())
  .then(data => {
    document.getElementById("userNameDisplay").textContent = data.username;
  });

// Carica categorie nella tendina
async function loadCategories() {
  const res = await fetch("http://localhost:8080/category/all", {
    credentials: "include"
  });

  const categories = await res.json();
  const select = document.getElementById("category");

  select.innerHTML = '<option value="">Seleziona una categoria</option>';

  categories.forEach(name => {
    const option = document.createElement("option");
    option.value = name;
    option.textContent = name;
    select.appendChild(option);
  });
}

loadCategories();

document.getElementById("transactionForm").addEventListener("submit", async function (e) {
  e.preventDefault();

  const category = document.getElementById("category").value;
  const type = JSON.parse(document.getElementById("type").value); // Converte "true" o "false" in booleano
  const amount = parseFloat(document.getElementById("amount").value);
  const description = document.getElementById("description").value;

  const transaction = {
    category,
    type,
    amount,
    description
  };

  try {
    const res = await fetch("http://localhost:8080/transaction/add", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      credentials: "include",
      body: JSON.stringify(transaction)
    });

    if (res.ok) {
	  document.getElementById("transactionForm").reset();
      alert("Transazione aggiunta con successo!");
      fetchTransactions();  // Ricarica la lista delle transazioni
      fetchTotals();  // Ricarica i totali
    } else {
      alert("Errore durante l'aggiunta della transazione.");
    }
  } catch (err) {
    console.error("Errore:", err);
    alert("Errore di rete o server.");
  }
});


async function fetchTransactions() {
  try {
    const res = await fetch("http://localhost:8080/transaction/all", {
      credentials: "include"
    });
    const transactions = await res.json();
    const table = document.getElementById("transactionsTable");
    table.innerHTML = "";

    transactions.forEach((t, index) => {
      const row = document.createElement("tr");

      row.innerHTML = `
        <td>${index + 1}</td>
        <td>${t.category}</td>
        <td>${t.date}</td>
        <td>${t.type === true ? "Entrata" : "Uscita"}</td>
        <td>${t.amount.toFixed(2)}</td>
        <td>${t.description || ""}</td>
		<td>
          <button class="delete-btn" data-id="${t.id}">🗑️</button>
        </td>
      `;

      table.appendChild(row);
    });

    // Event listener per i pulsanti delete
    document.querySelectorAll(".delete-btn").forEach(button => {
      button.addEventListener("click", async function () {
        const id = this.dataset.id;
        if (confirm("Sei sicuro di voler eliminare questa transazione?")) {
          await deleteTransaction(id);
        }
      });
    });


} catch (err) {
  console.error("Errore nel recupero delle transazioni:", err);
}
}




async function deleteTransaction(id) {
  try {
    const res = await fetch(`http://localhost:8080/transaction/${id}`, {
      method: "DELETE",
      credentials: "include"
    });

    if (res.ok) {
      fetchTransactions(); // aggiorna tabella
      fetchTotals(); // aggiorna totali
    } else {
      alert("Errore nell'eliminazione.");
    }
  } catch (err) {
    console.error("Errore durante l'eliminazione:", err);
  }
}

// Carica totali
async function fetchTotals() {
  try {
    const res = await fetch("http://localhost:8080/transaction/total", {
      credentials: "include"
    });

    const data = await res.json();

    const total = parseFloat(data.total) || 0;

    document.getElementById("totalIncome").textContent = `${total.toFixed(2)}€`;
    document.getElementById("totalExpenses").textContent = `0.00€`;
    document.getElementById("netProfit").textContent = `${total.toFixed(2)}€`;
    document.getElementById("cashBalance").textContent = `${total.toFixed(2)}€`;

  } catch (err) {
    console.error("Errore nel recupero dei totali:", err);
  }
}


fetchTransactions();
fetchTotals();