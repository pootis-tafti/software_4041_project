import axios from 'axios';

const isRunningInDocker = () => {
    return import.meta.env.VITE_RUNNING_IN_DOCKER === 'true';
};

const getBaseURL = () => {
    if (isRunningInDocker()) {
        return import.meta.env.VITE_API_BASE_URL || 'http://backend:9192';
    } else {
        return 'http://localhost:9192';
    }
};

export const api = axios.create({
    baseURL: getBaseURL()
});

export const getHeader = () => {
    const token = localStorage.getItem("token")
    if (!token) {
        console.warn('No JWT token found in localStorage');
        return {}
    }
    return {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json"
    }
}

/* This function adds a new room room to the database */
export async function addRoom(photo, roomType, roomPrice) {
    const formData = new FormData()
    formData.append("photo", photo)
    formData.append("roomType", roomType)
    formData.append("roomPrice", roomPrice)

    // const token = localStorage.getItem('jwtToken');
    const config = {
        headers: {
            ...getHeader(),
            'Content-Type': 'multipart/form-data'
        }
    };

    console.log('Request config:', config);

    try {
        const response = await api.post("/rooms/add/new-room", formData, config);
            // headers: {
            // 	'Content-Type': 'multipart/form-data',
            // 	'Authorization': `Bearer ${token}`
            // }
        return {
            success: true,
            status: response.status,
            data: response.data
        }
    } catch (error) {
        console.error("Error in addRoom:", error)
        return {
            success: false,
            status: error.response?.status,
            message: error.response?.data?.message || error.message
        }
        // throw error // Re-throw the error to be caught in handleSubmit
    }
}

/* This function gets all room types from thee database */
export async function getRoomTypes() {
    try {
        console.log("API Base URL:", api.defaults.baseURL)
        const response = await api.get("/rooms/room/types")
        console.log("API Response:", response)
        return response.data
    } catch (error) {
        console.error("Error in getRoomTypes:", error)
        throw new Error("Error fetching room types")
    }
}

/* This function gets all rooms from the database */
export async function getAllRooms() {
    try {
        const result = await api.get("/rooms/all-rooms")
        return result.data
    } catch (error) {
        throw new Error("Error fetching rooms")
    }
}

/* This function deletes a room by the Id */
export async function deleteRoom(roomId) {
    try {
        const result = await api.delete(`/rooms/delete/room/${roomId}`, {
            headers: getHeader()
        })
        return result.data
    } catch (error) {
        throw new Error(`Error deleting room ${error.message}`)
    }
}

/* This function update a room */
export async function updateRoom(roomId, roomData) {
    const formData = new FormData()
    formData.append("roomType", roomData.roomType)
    formData.append("roomPrice", roomData.roomPrice)
    formData.append("photo", roomData.photo)
    const response = await api.put(`/rooms/update/${roomId}`, formData, {
        headers: getHeader()
    })
    return response
}

/* This funcction gets a room by the id */
export async function getRoomById(roomId) {
    try {
        const result = await api.get(`/rooms/room/${roomId}`)
        return result.data
    } catch (error) {
        throw new Error(`Error fetching room ${error.message}`)
    }
}

/* This function saves a new booking to the database */
export async function bookRoom(roomId, booking) {
    try {
        const response = await api.post(`/bookings/room/${roomId}/booking`, booking)
        console.log(response.data)
        console.log(response.request)
        return response.data
    } catch (error) {
        if (error.response && error.response.data) {
            throw new Error(error.response.data)
        } else {
            throw new Error(`Error booking room : ${error.message}`)
        }
    }
}

/* This function gets all bookings from the database */
export async function getAllBookings() {
    try {
        const result = await api.get("/bookings/all-bookings", {
            headers: getHeader()
        })
        return result.data
    } catch (error) {
        throw new Error(`Error fetching bookings : ${error.message}`)
    }
}

/* This function get booking by the confirmation code */
export async function getBookingByConfirmationCode(confirmationCode) {
    try {
        const result = await api.get(`/bookings/confirmation/${confirmationCode}`)
        return result.data
    } catch (error) {
        if (error.response && error.response.data) {
            throw new Error(error.response.data)
        } else {
            throw new Error(`Error find booking : ${error.message}`)
        }
    }
}

/* This is the function to cancel user booking */
export async function cancelBooking(bookingId) {
    try {
        const result = await api.delete(`/bookings/booking/${bookingId}/delete`)
        return result.data
    } catch (error) {
        throw new Error(`Error cancelling booking :${error.message}`)
    }
}

/* This function gets all available rooms from the database with a given date and a room type */
export async function getAvailableRooms(checkInDate, checkOutDate, roomType) {
    const result = await api.get(
        `rooms/available-rooms?checkInDate=${checkInDate}
		&checkOutDate=${checkOutDate}&roomType=${roomType}`
    )
    return result
}

/* This function registers a new user */
export async function registerUser(registration) {
    try {
        const response = await api.post("/auth/register-user", registration)
        return response.data
    } catch (error) {
        if (error.reeponse && error.response.data) {
            throw new Error(error.response.data)
        } else {
            throw new Error(`User registration error : ${error.message}`)
        }
    }
}

/* This function login a registered user */
export async function loginUser(login) {
    try {
        const response = await api.post("/auth/login", login)
        if (response.status >= 200 && response.status < 300) {
            return response.data
        } else {
            return null
        }
    } catch (error) {
        console.error(error)
        return null
    }
}

/*  This is function to get the user profile */
export async function getUserProfile(userId, token) {
    try {
        const response = await api.get(`users/profile/${userId}`, {
            headers: getHeader()
        })
        return response.data
    } catch (error) {
        throw error
    }
}

/* This isthe function to delete a user */
export async function deleteUser(userId) {
    try {
        const response = await api.delete(`/users/delete/${userId}`, {
            headers: getHeader()
        })
        return response.data
    } catch (error) {
        return error.message
    }
}

/* This is the function to get a single user */
export async function getUser(userId, token) {
    try {
        const response = await api.get(`/users/${userId}`, {
            headers: getHeader()
        })
        return response.data
    } catch (error) {
        throw error
    }
}

/* This is the function to get user bookings by the user id */
export async function getBookingsByUserId(userId, token) {
    try {
        const response = await api.get(`/bookings/user/${userId}/bookings`, {
            headers: getHeader()
        })
        return response.data
    } catch (error) {
        console.error("Error fetching bookings:", error.message)
        throw new Error("Failed to fetch bookings")
    }
}
