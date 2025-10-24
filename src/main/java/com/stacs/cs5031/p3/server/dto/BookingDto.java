package com.stacs.cs5031.p3.server.dto;

import java.util.Date;
import java.util.List;

/**
 * Data Transfer Object for Booking entity.
 * Used for API requests and responses.
 */
public class BookingDto {
    private Long id;
    private String eventName;
    private Long roomId;
    private String roomName;
    private Date startTime;
    private int duration;
    private Long organiserId;
    private String organiserName;
    private List<AttendeeDto> attendees;
    private int currentAttendees;
    private int maxCapacity;

    // Default constructor
    public BookingDto() {
    }

    // Constructor with all fields
   public BookingDto(Long id,
                     String eventName,
                     Long roomId,
                     String roomName,
                     Date startTime,
                     int duration,
                     Long organiserId,
                     String organiserName,
                     List<AttendeeDto> attendees,
                     int currentAttendees,
                     int maxCapacity)
   {
        this.id = id;
        this.eventName = eventName;
        this.roomId = roomId;
        this.roomName = roomName;
        this.startTime = startTime;
        this.duration = duration;
        this.organiserId = organiserId;
        this.organiserName = organiserName;
        this.attendees = attendees;
        this.currentAttendees = currentAttendees;
        this.maxCapacity = maxCapacity;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Long getOrganiserId() {
        return organiserId;
    }

    public void setOrganiserId(Long organiserId) {
        this.organiserId = organiserId;
    }

    public String getOrganiserName() {
        return organiserName;
    }

    public void setOrganiserName(String organiserName) {
        this.organiserName = organiserName;
    }

    public List<AttendeeDto> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<AttendeeDto> attendees) {
        this.attendees = attendees;
    }

    public int getCurrentAttendees() {
        return currentAttendees;
    }

    public void setCurrentAttendees(int currentAttendees) {
        this.currentAttendees = currentAttendees;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    // Create BookingDTO from a Booking Request
    public static class BookingRequest {
        private String eventName;
        private Long roomId;
        private Date startTime;
        private int duration;
        private String title;          // Add this
        private String description;    // And this
        
        // Default constructor
        public BookingRequest() {
        }
        
        // Parameterized constructor
        public BookingRequest(String eventName, Long roomId, Date startTime, int duration, String title) {
            this.eventName = eventName;
            this.roomId = roomId;
            this.startTime = startTime;
            this.duration = duration;
            this.title = title;
        }

        public String getEventName() {
            return eventName;
        }

        public String getTitle() {
            return title;
        }

        public Long getRoomId() {
            return roomId;
        }

        public Date getStartTime() {
            return startTime;
        }

        public int getDuration() {
            return duration;
        }

        public void setEventName(String eventName) {
            this.eventName = eventName;
        }

        public void setRoomId(Long roomId) {
            this.roomId = roomId;
        }

        public void setStartTime(Date startTime) {
            this.startTime = startTime;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public String getDescription() {
            return description;
        }
    }
}










/*
 * package com.stacs.cs5031.p3.server.dto;

import java.util.Date;
import java.util.List;

/**
 * Data Transfer Object for Booking entity.
 * Used for API requests and responses.
 */
// public class BookingDto {
//     private Long id;
//     private String eventName;
//     private Long roomId;
//     private String roomName;
//     private Date startTime;
//     private int duration;
//     private Long organiserId;
//     private String organiserName;
//     private List<AttendeeDto> attendees;
//     private int currentAttendees;
//     private int maxCapacity;

//     // Default constructor
//     public BookingDto() {
//     }

//     // Constructor with all fields
//    public BookingDto(Long id,
//                      String eventName,
//                      Long roomId,
//                      String roomName,
//                      Date startTime,
//                      int duration,
//                      Long organiserId,
//                      String organiserName,
//                      List<AttendeeDto> attendees,
//                      int currentAttendees,
//                      int maxCapacity)
//    {
//         this.id = id;
//         this.eventName = eventName;
//         this.roomId = roomId;
//         this.roomName = roomName;
//         this.startTime = startTime;
//         this.duration = duration;
//         this.organiserId = organiserId;
//         this.organiserName = organiserName;
//         this.attendees = attendees;
//         this.currentAttendees = currentAttendees;
//         this.maxCapacity = maxCapacity;
//     }

//     // Getters and setters
//     public Long getId() {
//         return id;
//     }

//     public void setId(Long id) {
//         this.id = id;
//     }

//     public String getEventName() {
//         return eventName;
//     }

//     public void setEventName(String eventName) {
//         this.eventName = eventName;
//     }

//     public Long getRoomId() {
//         return roomId;
//     }

//     public void setRoomId(Long roomId) {
//         this.roomId = roomId;
//     }

//     public String getRoomName() {
//         return roomName;
//     }

//     public void setRoomName(String roomName) {
//         this.roomName = roomName;
//     }

//     public Date getStartTime() {
//         return startTime;
//     }

//     public void setStartTime(Date startTime) {
//         this.startTime = startTime;
//     }

//     public int getDuration() {
//         return duration;
//     }

//     public void setDuration(int duration) {
//         this.duration = duration;
//     }

//     public Long getOrganiserId() {
//         return organiserId;
//     }

//     public void setOrganiserId(Long organiserId) {
//         this.organiserId = organiserId;
//     }

//     public String getOrganiserName() {
//         return organiserName;
//     }

//     public void setOrganiserName(String organiserName) {
//         this.organiserName = organiserName;
//     }

//     public List<AttendeeDto> getAttendees() {
//         return attendees;
//     }

//     public void setAttendees(List<AttendeeDto> attendees) {
//         this.attendees = attendees;
//     }

//     public int getCurrentAttendees() {
//         return currentAttendees;
//     }

//     public void setCurrentAttendees(int currentAttendees) {
//         this.currentAttendees = currentAttendees;
//     }

//     public int getMaxCapacity() {
//         return maxCapacity;
//     }

//     public void setMaxCapacity(int maxCapacity) {
//         this.maxCapacity = maxCapacity;
//     }

//     // Create BookingDTO from a Booking Request
//     public static class BookingRequest {
//         private String eventName;
//         private Long roomId;
//         private Date startTime;
//         private int duration;

//         public String getEventName() {
//             return eventName;
//         }

//         public Long getRoomId() {
//             return roomId;
//         }

//         public Date getStartTime() {
//             return startTime;
//         }

//         public int getDuration() {
//             return duration;
//         }

//         public void setEventName(String eventName) {
//             this.eventName = eventName;
//         }

//         public void setRoomId(Long roomId) {
//             this.roomId = roomId;
//         }

//         public void setStartTime(Date startTime) {
//             this.startTime = startTime;
//         }

//         public void setDuration(int duration) {
//             this.duration = duration;
//         }
//     }
// }
//  */
