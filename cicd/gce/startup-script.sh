#! /bin/bash
#Install log agent
apt-get update && apt-get upgrade -y
curl -sSO https://dl.google.com/cloudagents/add-logging-agent-repo.sh
sudo bash add-logging-agent-repo.sh
sudo apt-get update
sudo apt-get install google-fluentd
sudo apt-get install -y google-fluentd-catch-all-config-structured
sudo service google-fluentd start

apt-get update
apt-get install -y apache2
cat <<EOF > /var/www/html/index.html
<html><body><h1>custom-instance-template-1</h1>
<p> Hello World</p>
</body></html>
EOF
