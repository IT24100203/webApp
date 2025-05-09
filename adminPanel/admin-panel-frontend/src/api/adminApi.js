import axios from "axios";

const BASE = "http://localhost:8080/api/admin";

// ---------- AUTH ----------
export function login(data) {
    return axios.post(`${BASE}/login`, data);
}

// ---------- TABLES ----------
export function getTables() {
    return axios.get(`${BASE}/tables`);
}

export function addTable(data) {
    return axios.post(`${BASE}/tables`, data);
}

export function deleteTable(id) {
    return axios.delete(`${BASE}/tables/${id}`);
}

// ---------- RESERVATIONS ----------
export function getReservations(sort) {
    const url = sort
        ? `${BASE}/reservations?sorted=true`
        : `${BASE}/reservations`;
    return axios.get(url);
}

export function overrideReservation(reservation) {
    return axios.put(`${BASE}/reservations`, reservation);
}

export function purgeReservations() {
    return axios.delete(`${BASE}/reservations/purge`);
}

// ---------- REPORT ----------
export function getReport(date) {
    return axios.get(`${BASE}/reports?date=${date}`);
}
