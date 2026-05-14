@echo off

start cmd /k "cd /d D:\HoaEpProject\FE\dear-floral-bloom && npm run dev"
start powershell -NoExit -Command "cd 'D:\HoaEpProject\BE\dear-floral-backend'; ./run.ps1"