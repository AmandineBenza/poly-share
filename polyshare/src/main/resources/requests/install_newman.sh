#!/bin/sh

echo "---- [WARNING] Requires Linux ---- ";
echo "---- Postman command line. Required installation ----";
echo ">> Updating packages...";
sleep 1;
sudo apt update;
echo ">> Done."
sleep 1;
echo ">> Installing nodejs...";
sleep 1;
sudo apt install nodejs npm;
sleep 1;
sudo apt install nodejs-legacy;
sleep 3;
echo ">> Done.";
sleep 1;
echo ">> Installing npm...";
sleep 1;
sudo apt-get install npm;
sleep 1;
sudo npm install -g n;
sudo n latest;
sleep 3;
echo ">> Done."
sleep 2;
echo ">> Installing newman...";
sudo npm install -g newman
sleep 3;
echo ">> Done."
sleep 1;
echo ">> Launch your postman collection with \"sudo newman run Poly-share.postman_collection.json\" or \"sh ./run_newman.sh\"."
sleep 1;
echo ">> If you encounter difficulties. Please execute the following command and re-launch the script:"
sleep 1;
echo ">> \"sudo ln -s /usr/bin/nodejs /usr/bin/node\"."
echo "-----------------"
