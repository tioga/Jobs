@echo off
del /f/q \\parwinproto\d$\is-apps\tioga-jobs-agent-grizzly\lib\*.jar
copy /Y C:\dvlp\3rd-party\tioga-solutions\Jobs\tioga-jobs-agent-grizzly\build\install\tioga-jobs-agent-grizzly\lib\*.jar \\parwinproto\d$\is-apps\tioga-jobs-agent-grizzly\lib
copy /Y C:\dvlp\3rd-party\tioga-solutions\Jobs\tioga-jobs-agent-grizzly\build\install\tioga-jobs-agent-grizzly\server-start.bat \\parwinproto\d$\is-apps\tioga-jobs-agent-grizzly\
