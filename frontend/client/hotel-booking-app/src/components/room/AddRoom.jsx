import React, {useState} from "react"
import {addRoom, api} from "../utils/ApiFunctions"
import RoomTypeSelector from "../common/RoomTypeSelector"
import {Link} from "react-router-dom"

const AddRoom = () => {
    const [newRoom, setNewRoom] = useState({
        photo: null,
        roomType: "",
        roomPrice: ""
    })

    const [successMessage, setSuccessMessage] = useState("")
    const [errorMessage, setErrorMessage] = useState("")
    const [imagePreview, setImagePreview] = useState("")

    const handleRoomInputChange = (e) => {
        const name = e.target.name
        let value = e.target.value
        if (name === "roomPrice") {
            if (!isNaN(value)) {
                value = parseInt(value)
            } else {
                value = ""
            }
        }
        setNewRoom({...newRoom, [name]: value})
    }

    const handleImageChange = (e) => {
        const selectedImage = e.target.files[0]
        setNewRoom({...newRoom, photo: selectedImage})
        setImagePreview(URL.createObjectURL(selectedImage))
    }

    // const handleSubmit = async (e) => {
    //     e.preventDefault()
    //     try {
    //         const success = await addRoom(newRoom.photo, newRoom.roomType, newRoom.roomPrice)
    //         if (success !== undefined) {
    //             setSuccessMessage("A new room was  added successfully !")
    //             setNewRoom({photo: null, roomType: "", roomPrice: ""})
    //             setImagePreview("")
    //             setErrorMessage("")
    //         } else {
    //             setErrorMessage("Error adding new room")
    //         }
    //     } catch (error) {
    //         console.error("Error adding room:", error)
    //         setErrorMessage(error.message)
    //         // setErrorMessage(error.response?.data || "Error adding new room")
    //     }
    //     setTimeout(() => {
    //         setSuccessMessage("")
    //         setErrorMessage("")
    //     }, 3000)
    // }

    const handleSubmit = async (e) => {
        e.preventDefault()
        try {
            const result = await addRoom(newRoom.photo, newRoom.roomType, newRoom.roomPrice)
            if (result.success) {
                setSuccessMessage("A new room was added successfully!")
                setNewRoom({photo: null, roomType: "", roomPrice: ""})
                setImagePreview("")
                setErrorMessage("")
            } else {
                // The room might have been added despite an error response
                if (result.status === 201) {
                    setSuccessMessage("The room may have been added successfully, but there was an issue with the response.")
                } else {
                    setErrorMessage(`Error adding new room: ${result.message}`)
                }
            }
        } catch (error) {
            console.error("Error in handleSubmit:", error)
            setErrorMessage("An unexpected error occurred. The room may or may not have been added.")
        }
        setTimeout(() => {
            setSuccessMessage("")
            setErrorMessage("")
        }, 5000)
    }

    return (
        <>
            <section className="container mt-5 mb-5">
                <div className="row justify-content-center">
                    <div className="col-md-8 col-lg-6">
                        <h2 className="mt-5 mb-2">Add a New Room</h2>
                        {successMessage && (
                            <div className="alert alert-success fade show"> {successMessage}</div>
                        )}

                        {errorMessage && <div className="alert alert-danger fade show"> {errorMessage}</div>}

                        <form onSubmit={handleSubmit}>
                            <div className="mb-3">
                                <label htmlFor="roomType" className="form-label">
                                    Room Type
                                </label>
                                <div>
                                    <RoomTypeSelector
                                        handleRoomInputChange={handleRoomInputChange}
                                        newRoom={newRoom}
                                    />
                                </div>
                            </div>
                            <div className="mb-3">
                                <label htmlFor="roomPrice" className="form-label">
                                    Room Price
                                </label>
                                <input
                                    required
                                    type="number"
                                    className="form-control"
                                    id="roomPrice"
                                    name="roomPrice"
                                    value={newRoom.roomPrice}
                                    onChange={handleRoomInputChange}
                                />
                            </div>

                            <div className="mb-3">
                                <label htmlFor="photo" className="form-label">
                                    Room Photo
                                </label>
                                <input
                                    required
                                    name="photo"
                                    id="photo"
                                    type="file"
                                    className="form-control"
                                    onChange={handleImageChange}
                                />
                                {imagePreview && (
                                    <img
                                        src={imagePreview}
                                        alt="Preview  room photo"
                                        style={{maxWidth: "400px", maxHeight: "400px"}}
                                        className="mb-3"></img>
                                )}
                            </div>
                            <div className="d-grid gap-2 d-md-flex mt-2">
                                <Link to={"/existing-rooms"} className="btn btn-outline-info">
                                    Existing rooms
                                </Link>
                                <button type="submit" className="btn btn-outline-primary ml-5">
                                    Save Room
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </section>
        </>
    )
}

export default AddRoom
