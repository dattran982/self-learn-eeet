import axios from "axios";

const API_BASE = "http://localhost:8080/api/students";

export async function getAllStudents() {
  return axios.get(`${API_BASE}/getAll`);
}

export async function saveStudent(student) {
  return axios.post(`${API_BASE}/save`, student);
}

export async function updateStudent(studentId, student) {
  return axios.post(`${API_BASE}/edit/${studentId}`, student);
}

export async function deleteStudentById(studentId) {
  return axios.delete(`${API_BASE}/delete/${studentId}`);
}
