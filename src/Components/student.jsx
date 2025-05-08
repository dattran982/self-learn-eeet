import { useEffect, useState } from "react";
import {
  getAllStudents,
  saveStudent,
  updateStudent,
  deleteStudentById,
} from "../Appapi";

function Student() {
  const [studentId, setId] = useState(""); // Store ID for updating
  const [studentName, setName] = useState("");
  const [studentAddress, setAddress] = useState("");
  const [mobile, setMobile] = useState("");
  const [students, setStudents] = useState([]); // Array to store all students
  const [searchTerm, setSearchTerm] = useState(""); // State for search input
  const [currentPage, setCurrentPage] = useState(1);
  const studentsPerPage = 5;

  useEffect(() => {
    (async () => await load())(); // Load students on component mount
  }, []);

  // Function to load all students
  async function load() {
    try {
      const result = await getAllStudents();
      setStudents(result.data); // Populate students array with data from API
      setCurrentPage(1); // Reset to first page on reload
      console.log("Loaded students:", result.data);
    } catch (err) {
      console.error("Failed to load students:", err);
    }
  }

  // Function to validate form fields based on backend model constraints
  function validateForm() {
    // Name: not blank, min 2 chars, only letters and spaces
    if (!studentName.trim()) {
      alert("Name cannot be blank");
      return false;
    }
    if (studentName.trim().length < 2) {
      alert("Name should have at least 2 characters");
      return false;
    }
    if (!/^[a-zA-Z ]*$/.test(studentName.trim())) {
      alert("Name should only contain letters and spaces");
      return false;
    }

    // Address: not blank, min 5 chars
    if (!studentAddress.trim()) {
      alert("Address cannot be blank");
      return false;
    }
    if (studentAddress.trim().length < 5) {
      alert("Address should have at least 5 characters");
      return false;
    }

    // Mobile: not null, 10-15 digits, only digits
    if (!mobile.trim()) {
      alert("Mobile number cannot be blank");
      return false;
    }
    if (!/^\d{10,15}$/.test(mobile.trim())) {
      alert(
        "Mobile number should be between 10 and 15 digits and contain only numbers"
      );
      return false;
    }

    return true;
  }

  // Function to save a new student
  async function save(event) {
    event.preventDefault();

    if (!validateForm()) return;

    try {
      await saveStudent({
        studentName,
        studentAddress,
        mobile,
      });
      alert("Student Registration Successful");
      resetForm(); // Clear the input fields after saving
      load(); // Reload the student list
    } catch (err) {
      if (err.response && err.response.data && err.response.data.message) {
        // Display backend validation error message
        alert(`Error: ${err.response.data.message}`);
      } else {
        // Handle other errors
        alert("Student Registration Failed");
        console.error("Error during registration:", err);
      }
    }
  }

  // Function to edit a student (populate fields for editing)
  function editStudent(student) {
    console.log("Editing student:", student); // Debugging log
    setId(student.studentId); // Set the student ID for update
    setName(student.studentName);
    setAddress(student.studentAddress);
    setMobile(student.mobile);
  }

  // Function to delete a student
  async function deleteStudent(studentid) {
    try {
      await deleteStudentById(studentid);
      alert("Student deleted successfully");
      load(); // Reload the student list after deletion
    } catch (err) {
      alert("Failed to delete student");
      console.error("Error during deletion:", err);
    }
  }

  // Function to update a student
  async function update(event) {
    event.preventDefault();
    if (!studentId) {
      alert("No student selected for update.");
      return;
    }
    if (!validateForm()) return;
    try {
      await updateStudent(studentId, {
        studentName,
        studentAddress,
        mobile,
      });
      alert("Student Updated Successfully");
      resetForm(); // Clear the form fields
      load(); // Reload the student list after updating
    } catch (err) {
      alert("Failed to update student");
      console.error("Error during update:", err);
    }
  }

  // Function to reset the form fields
  function resetForm() {
    setId("");
    setName("");
    setAddress("");
    setMobile("");
  }

  // Filter students based on search term
  const filteredStudents = students.filter(
    (student) =>
      student.studentName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      student.studentAddress.toLowerCase().includes(searchTerm.toLowerCase()) ||
      student.mobile.toLowerCase().includes(searchTerm.toLowerCase())
  );

  // Pagination logic
  const indexOfLastStudent = currentPage * studentsPerPage;
  const indexOfFirstStudent = indexOfLastStudent - studentsPerPage;
  const currentStudents = filteredStudents.slice(
    indexOfFirstStudent,
    indexOfLastStudent
  );
  const totalPages = Math.ceil(filteredStudents.length / studentsPerPage);

  const handlePageChange = (pageNumber) => {
    setCurrentPage(pageNumber);
  };

  return (
    <div className="container mt-4">
      <h1 className="text-center">Student Details</h1>

     
      <form>
       
    
        <div className="form-group">
          <label>Student Name</label>
          <input
            type="text"
            className="form-control"
            value={studentName}
            onChange={(e) => setName(e.target.value)}
          />
        </div>

        <div className="form-group">
          <label>Student Address</label>
          <input
            type="text"
            className="form-control"
            value={studentAddress}
            onChange={(e) => setAddress(e.target.value)}
          />
        </div>

        <div className="form-group">
          <label>Mobile</label>
          <input
            type="text"
            className="form-control"
            value={mobile}
            onChange={(e) => setMobile(e.target.value)}
          />
        </div>

        <div className="mt-3">
          <button className="btn btn-primary me-2" onClick={save}>
            Register
          </button>
          <button className="btn btn-warning" onClick={update}>
            Update
          </button>
        </div>
      </form>

      <br />
      <div className="mb-3">
        <input
          type="text"
          className="form-control"
          placeholder="Search by name, address, or mobile"
          value={searchTerm}
          onChange={(e) => {
            setSearchTerm(e.target.value);
            setCurrentPage(1); // Reset to first page on search
          }}
        />
      </div>

      <table className="table table-dark text-center">
        <thead>
          <tr>
            <th>Student ID</th>
            <th>Student Name</th>
            <th>Student Address</th>
            <th>Student Mobile</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {currentStudents.map((student) => (
            <tr key={student.studentId}>
              <td>{student.studentId}</td>
              <td>{student.studentName}</td>
              <td>{student.studentAddress}</td>
              <td>{student.mobile}</td>
              <td>
                <button
                  className="btn btn-warning me-2"
                  onClick={() => editStudent(student)}
                >
                  Edit
                </button>
                <button
                  className="btn btn-danger"
                  onClick={() => deleteStudent(student.studentid)}
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {/* Pagination Controls */}
      <nav>
        <ul className="pagination justify-content-center">
          <li className={`page-item${currentPage === 1 ? " disabled" : ""}`}>
            <button
              className="page-link"
              onClick={() => handlePageChange(currentPage - 1)}
              disabled={currentPage === 1}
            >
              Previous
            </button>
          </li>
          {[...Array(totalPages)].map((_, idx) => (
            <li
              key={idx + 1}
              className={`page-item${currentPage === idx + 1 ? " active" : ""}`}
            >
              <button
                className="page-link"
                onClick={() => handlePageChange(idx + 1)}
              >
                {idx + 1}
              </button>
            </li>
          ))}
          <li
            className={`page-item${
              currentPage === totalPages || totalPages === 0 ? " disabled" : ""
            }`}
          >
            <button
              className="page-link"
              onClick={() => handlePageChange(currentPage + 1)}
              disabled={currentPage === totalPages || totalPages === 0}
            >
              Next
            </button>
          </li>
        </ul>
      </nav>
    </div>
  );
}

export default Student;
