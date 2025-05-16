document.addEventListener("DOMContentLoaded", async () => {
    try {
        // Carica i dati dell'utente
        const response = await fetch("http://localhost:8080/users/me", {
            credentials: "include"
        });
        
        if (!response.ok) {
            throw new Error("Errore nel caricamento del profilo");
        }
        
        const userData = await response.json();
        
        // Popola il form
        document.getElementById("name").value = userData.username || "";
        document.getElementById("email").value = userData.email || "";
        
        // Configura l'event listener per il salvataggio
        document.getElementById("editProfileForm").addEventListener("submit", async (e) => {
            e.preventDefault();
            
            const updates = {
                username: document.getElementById("name").value.trim(),
                email: document.getElementById("email").value.trim(),
                password: document.getElementById("password").value
            };
            
            try {
                const updateResponse = await fetch("http://localhost:8080/users/update", {
                    method: "PUT",
                    headers: { "Content-Type": "application/json" },
                    credentials: "include",
                    body: JSON.stringify(updates)
                });
                
                const result = await updateResponse.json();
                
                if (updateResponse.ok) {
                    alert("Profilo aggiornato con successo!");
                    window.location.href = "index.html";
                } else {
                    throw new Error(result.message || "Errore nell'aggiornamento");
                }
            } catch (error) {
                console.error("Errore:", error);
                alert(error.message);
            }
        });
    } catch (error) {
        console.error("Errore nel caricamento iniziale:", error);
        alert("Impossibile caricare i dati del profilo");
        window.location.href = "index.html";
    }
});