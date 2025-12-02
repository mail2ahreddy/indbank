function openGrafana() {
  window.open("http://localhost:3000", "_blank");
}

function openJaeger() {
  window.open("http://localhost:16686", "_blank");
}

function openKafkaUI() {
  window.open("http://localhost:8081", "_blank");
}

async function loadSmsLogs() {
  const res = await fetch("/api/logs/sms");
  const txt = await res.text();
  document.getElementById("smsLogs").textContent = txt;
}

// Existing function: reload SMS cards
async function loadSmsNotifications() {
  const res = await fetch("/api/sms");
  const data = await res.json();

  const container = document.getElementById("smsContainer");
  container.innerHTML = "";

  data.forEach(sms => {
    container.innerHTML += `
      <div class="sms-card">
        <p><b>Type:</b> ${sms.type}</p>
        <p><b>Account:</b> ${sms.account}</p>
        <p><b>Amount:</b> ${sms.amount}</p>
        <p>${sms.timestamp}</p>
      </div>
    `;
  });
}

setInterval(loadSmsNotifications, 2000);
loadSmsNotifications();
