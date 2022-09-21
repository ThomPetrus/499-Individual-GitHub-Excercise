# 499-Individual-GitHub-Exercise
A repository containing the individual GitHub exercise for `COSC499`

Simple SpringBoot application with four endpoints. 
Allows the user to set and retrieve messages from redis!

---
<h3>Dependencies</h3>

* Java 11
* [Docker](https://docs.docker.com/engine/install/)
* [Docker Compose](https://www.digitalocean.com/community/tutorials/how-to-install-and-use-docker-compose-on-ubuntu-20-04)
* Maven
```
sudo apt-get install maven
```
---
<h3>How to Run</h3>

Build and start the application with:
```
./exercise.sh run
```

Run tests with:
```
./exercise.sh test
```
---
<h3>Troubleshooting</h3>

* If you run into issues running the script, make sure the script has the right permissions to execute.
* If you run into issues with docker-compose (in particular when using Ubuntu), make sure you are running docker desktop
  and are logged in. Again, if on Ubuntu, see this [docker article](/home/petrus/Documents/IDE/idea-IC-222.3739.54/bin).
  If you still have issues, see this [SO post](https://stackoverflow.com/questions/56784492/permissionerror-errno-13-permission-denied-manage-py).

---
<h3>Example Requests</h3>

```
curl --location --request GET 'http://localhost:8010/message?key=bla233333'
curl --location --request PUT 'http://localhost:8010/message?key=bla233333&message=something'
curl --location --request GET 'http://localhost:8010/message/all'
curl --location --request DELETE 'http://localhost:8010/message/all'
```