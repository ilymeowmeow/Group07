FROM gcc:latest AS builder

WORKDIR /app

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

RUN mkdir -p output && \
    g++ -std=c++17 -Wall -Wextra -g \
    main.cpp \
    AuthService.cpp \
    adminmodule.cpp \
    StudentView.cpp \
    Teacher.cpp \
    -o output/main


FROM gcc:latest

WORKDIR /app


COPY --from=builder /app/output/main .


COPY data/ ./data/

ENV LD_LIBRARY_PATH=/usr/local/lib:/usr/lib

CMD ["./main"]
