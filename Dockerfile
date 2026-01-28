# Multi-stage build for Student Attendance Management System
FROM gcc:latest AS builder

WORKDIR /app

# Copy all source files
COPY src/main.cpp .
COPY src/AuthService.cpp .
COPY src/AuthService.h .
COPY src/adminmodule.cpp .
COPY src/adminmodule.h .
COPY src/StudentView.cpp .
COPY src/StudentView.h .
COPY src/Teacher.cpp .
COPY src/Teacher.h .
COPY src/adminmodule.h ./AdminModule.h

# Create output directory and build the application
RUN mkdir -p output && \
    g++ -std=c++17 -Wall -Wextra -g \
    main.cpp \
    AuthService.cpp \
    adminmodule.cpp \
    StudentView.cpp \
    Teacher.cpp \
    -o output/main

# Runtime stage
FROM gcc:latest

WORKDIR /app

# Copy the compiled executable from builder
COPY --from=builder /app/output/main .

# Copy data files
COPY data/ ./data/

# Set environment variables
ENV LD_LIBRARY_PATH=/usr/local/lib:/usr/lib

# Run the application
CMD ["./main"]
