<form id="registerForm">
    <input type="text" id="nome" placeholder="Nome Completo" required>
    <input type="email" id="email" placeholder="Email" required>
    <input type="password" id="password" placeholder="Password" required>
    <button type="submit">Registrati</button>
</form>

<script>
    document.getElementById("registerForm").addEventListener("submit", function(event) {
        event.preventDefault(); // Evita il reload della pagina
        
        const nome = document.getElementById("nome").value;
        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;

        fetch("register.php", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ nome, email, password })
        })
        .then(response => response.json())
        .then(data => alert(data.messaggio))  // Mostra la risposta dal server
        .catch(error => console.error("Errore:", error));
    });
</script>
