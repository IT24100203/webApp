import axios from "axios";

const BASE = "http://localhost:8080/api/dashboard";

export function getReport(date) {
    return axios.get(`${BASE}/report?date=${date}`);
}

export function purgeOldReservations() {
    return axios.delete(`${BASE}/purge`);
}
