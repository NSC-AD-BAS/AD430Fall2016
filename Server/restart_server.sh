#!/usr/bin/env bash
sudo killall nodejs
echo "Stopping Server"
sudo nohup nodejs /home/expressserver/ad430_server/app.js &
echo "Server Restarted"
exit
