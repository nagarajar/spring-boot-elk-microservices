# spring-boot-elk-microservices
A complete hands-on project demonstrating real-time log monitoring using Spring Boot microservices integrated with the ELK Stack (Elasticsearch, Logstash, Kibana). Includes Docker-based ELK setup, Logstash pipeline configuration, structured JSON logging, and dashboards for centralized log analysis.

---
# 🧭 Training Plan (Like a Real Mentor)

We will build this project in **6 phases**, one by one.


## 🎓 Phase 1 — Architecture Setup & Understanding


## 🎓 Phase 2 — Prepare ELK Using Docker (Hands-On)


## 🎓 Phase 3 — Create 3 Spring Boot Microservices


## 🎓 Phase 4 — Configure Logback to Send Logs to Logstash


## 🎓 Phase 5 — Create Logstash Pipeline


## 🎓 Phase 6 — Create Kibana Dashboards & Visualizations

---

# ✅ PHASE 1 — Architecture Setup & Understanding 
## What is ELK Stack and Why Do We Use It?

ELK is in high demand in Spring Boot applications due to its powerful logging and monitoring capabilities.  
The **ELK stack** is a collection of three open-source products:

- **Elasticsearch** → Works as a search engine and NoSQL database. It can search and analyze large collections of data.  
- **Logstash** → A log pipeline tool that accepts data, processes it into different parts, and exports it to target locations.  
- **Kibana** → A visualization layer (UI) that sits above Elasticsearch, allowing you to explore and visualize logs.  



## 🏗 Spring Boot + ELK Stack

For example:  
Suppose there are **100 applications** running, all generating logs in the same target location.  
If you need a single log file from one particular application, finding it manually would be tedious.  

➡ With **ELK**, you can search and filter logs in **real-time** without inconvenience.


## 📊 Architecture Diagram

![ELK Stack Logo](./images/spring-boot-elk-stack-architecture.jpg)


---



# 📖 Phase 2 — ELK docker-compose Deep Dive

Absolutely! I will explain Phase 2 (ELK docker-compose) and Phase 3 (logback-spring.xml) line-by-line like a real trainer so you understand every component clearly.



## 🧭 PHASE 2 — Deep Explanation of ELK docker-compose.yml



### 🟦 PART 1: Understanding Docker Compose Structure

The docker-compose.yml defines three containers:

- **Elasticsearch** → Stores & indexes logs  
- **Kibana** → User UI to view logs  
- **Logstash** → Reads incoming logs & forwards to Elasticsearch  

All three share a network called **elk** so they can communicate internally.



### 🟨 PART 2: Understanding Each Section in Detail

#### 1️⃣ Elasticsearch Service

**Image:** `docker.elastic.co/elasticsearch/elasticsearch:8.12.0`  
✔ Why image 8.12.0?  
- Version 8.x is stable and widely used.

**Container name:** `elasticsearch`  
- You give a name instead of auto-generated one.  
- Makes debugging easier.

**Environment Variables:**  
- `discovery.type=single-node`  
  - Tells Elasticsearch to run in single-node mode  
  - ➡ No cluster formation  
  - ➡ Perfect for local development / training  

- `xpack.security.enabled=false`  
  - Disables security (passwords/authentication).  
  - ➡ For learning mode this is fine  
  - ➡ In production, this MUST be enabled!  

- `ES_JAVA_OPTS=-Xms1g -Xmx1g`  
  - Sets JVM heap size for Elasticsearch.  
  - Xms = minimum heap  
  - Xmx = maximum heap  
  - Elasticsearch needs RAM, ideally:  
    - Minimum → 1GB  
    - Recommended → 2GB+  

**Port Mapping:**  
- `9200:9200` → Elasticsearch REST API  
- Access in browser: `http://localhost:9200`  

**Volume:**  
- `es-data:/usr/share/elasticsearch/data`  
- Purpose: Saves Elasticsearch indexes  
- If you restart Docker, your logs DO NOT disappear  

**Network:**  
- `elk` → All services communicate in this private network  



#### 2️⃣ Kibana Service

**Image:** `docker.elastic.co/kibana/kibana:8.12.0`  
- Kibana UI to view log dashboards.

**Ports:**  
- `5601:5601` → Access Kibana UI at `http://localhost:5601`  

**Depends_on:**  
- Ensures Elasticsearch must start first  
- Kibana waits until Elasticsearch is running  

**Environment:**  
- `ELASTICSEARCH_HOSTS=http://elasticsearch:9200`  
- Kibana connects to Elasticsearch inside the docker network using `elasticsearch:9200`  



#### 3️⃣ Logstash Service

**Image:** `docker.elastic.co/logstash/logstash:8.12.0`  
- Logstash receives logs from Spring Boot and forwards to Elasticsearch.

**Port Mapping:**  
- `5044:5044` → This is the port where Spring Boot sends logs using Logback TCP.  

**Pipeline Mount:**  
- `./logstash/pipeline:/usr/share/logstash/pipeline`  
- Meaning: Your local folder `logstash/pipeline` is copied inside the container  
- So you can define pipelines like `logstash.conf`  



#### 🔹 Networks & Volumes Definitions

At bottom:  

- **Volumes:**  
  - `es-data` → Creates persistent storage  

- **Networks:**  
  - `elk` → All services share a private network called “elk”  

#### 🚀 How to Run ELK Stack

Run the following in your project folder:
```
docker-compose up -d
```


Check running containers:
```
docker ps
```

🌐 Access Points After Start
Component	URL
 - Elasticsearch	http://localhost:9200

- Kibana Dashboard	http://localhost:5601

- Logstash Input	tcp://localhost:5044

#### 🛠 Useful Commands
Stop containers:

```
docker-compose down
```
View logs:

```
docker-compose logs -f
```
Restart containers:

```
docker-compose restart
```

#### 📚 References
- [Elasticsearch Documentation](https://www.elastic.co/docs/get-started)

- [Kibana Documentation](https://www.elastic.co/docs/get-started/the-stack)

- [Logstash Documentation](https://www.elastic.co/docs/reference/logstash)

---

# 🎓 Phase 3 — Create 4 Spring Boot Microservices
- Eureka Server

- Product Service

- Order Service

- Payment Service

# 📘 Eureka Server – Project Description (Spring Boot 3 + Java 17)

This project is the **Service Discovery Server** built using **Spring Boot 4** and **Spring Cloud Netflix Eureka**.  
It plays a central role in the microservices architecture by enabling dynamic discovery and registration of services.



## 🔥 Key Responsibilities of Eureka Server

- Maintains a central registry of all active microservices.
- Allows services (Product, Order, Payment) to discover each other without hard-coding URLs.
- Provides a web dashboard at [http://localhost:8761](http://localhost:8761) for:
    - Viewing registered instances
    - Instance health status
    - Heartbeat monitoring



## 🧩 Tech Stack

- Spring Boot 3.5.x
- Spring Cloud Netflix Eureka Server
- Spring Web (required for Eureka UI)
- Java 17
- Maven



## 📦 Included Dependencies

- `spring-cloud-starter-netflix-eureka-server`
- `spring-boot-starter-web` *(Eureka dashboard requires a web environment)*



## 🚀 Purpose in the Architecture

Eureka Server is the **first microservice to start** in the entire system.  
All other services (Product, Order, Payment) will:

- ✔ Register themselves with Eureka
- ✔ Fetch other service locations dynamically
- ✔ Communicate without knowing actual server IP/port

This makes the architecture **scalable**, **fault-tolerant**, and **cloud-ready**.

---