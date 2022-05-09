const pathNames = window.location.pathname.split("/");
const id = pathNames[2];

window.onload = async () => {
    const data = await fetch("/chess-games/" + id + "/status", {
        method: "GET"
    })
    .then(r=>r.json())
    .then(data => {
        return data;
    });

    JsonSender.setScores(data);
}

const JsonSender = {
    setScores: function (scores) {
        const whiteScore = scores.whiteScore;
        const blackScore = scores.blackScore;

        const scoreSection = document.querySelector(".score");
        const tr = scoreSection.querySelector("tbody tr");

        let html = '';
        html += '<td>' + whiteScore + '</td>';
        html += '<td>' + blackScore + '</td>';
        tr.innerHTML = html;
    }
}

function lobby() {
    window.location.replace("/");
}

async function start() {
    await fetch("/chess-games/" + id + "/initialization", {
        method: "PUT"
    });

    window.location.replace("/chess-games/" + id);
}