# 🧠 Software Development AI Agent

This repository contains an AI-powered agent system that automates key phases of the Software Development Lifecycle (SDLC), including cloning, building, reviewing, and security scanning of source code.

It uses:

- **Google Agent Development Kit (ADK)** for orchestrating LLM-based agents
- **Java Spring Boot** for issue logging and service coordination
- **Custom callbacks** for error handling and sequential flow control

---

## 🌐 Related Repositories

| Repository Name         | GitHub Link                                                                                             | Server Port | DB Port |
|-------------------------|---------------------------------------------------------------------------------------------------------|-------------|---------|
| Case Management Service | [🔗 Case Management Service](https://github.com/brandontan2003/gcp25_ai_agent_case_management_service)  | `8081`      | `3307`  |
| AI Agent Common Core    | [🔗 AI Agent Common Core](https://github.com/brandontan2003/gcp25_ai_agent_common_core)                 | `N/A`       | `N/A`   |
| AI Agent                | [🔗 AI Agent](https://github.com/brandontan2003/gcp25_ai_agent_hackathon)                               | `8082`      | `N/A`   |

---

## 📦 Getting Started

### Prerequisites

- Java 11+
- Gradle 6.x+
- Docker
- API key from [Google AI Studio](https://aistudio.google.com/apikey)
- AI Agent Common Core module - [GitHub Repo](https://github.com/brandontan2003/gcp25_ai_agent_common_core)
- Case Management Service - [GitHub Repo](https://github.com/brandontan2003/gcp25_ai_agent_case_management_service)

> **Important:**
> - Clone and publish the **AI Agent Common Core** module to your local Maven repository:  
   [AI Agent Common Core GitHub Repo](https://github.com/brandontan2003/gcp25_ai_agent_common_core)   
>   ```bash
>   git clone https://github.com/brandontan2003/gcp25_ai_agent_common_core.git
>   cd gcp25_ai_agent_common_core
>   ./gradlew clean build publishToMavenLocal
>   ```  
> - Please ensure the **Case Management Service** is running before starting this project.  
    [Case Management Service GitHub Repo](https://github.com/brandontan2003/gcp25_ai_agent_case_management_service)

### 🛠️ Installation

```bash
# Clone the repository
git clone https://github.com/brandontan2003/gcp25_ai_agent_hackathon.git
cd gcp25_ai_agent_hackathon
```

### Local Setup
1. Set the API Key in the startup.sh file:
GOOGLE_API_KEY = 
2. Run the startup script:
```bash
./scripts/startup.sh
```


### Docker Setup
1. Set the API Key in the docker.env file:
   GOOGLE_API_KEY =
2. Build and start the containers:
```bash
docker compose up -d --build
```

---

### 📚 More Information

For detailed information on the **Google Agent Development Kit (ADK)**, please refer to the official documentation:

- [Google ADK Documentation](https://google.github.io/adk-docs/)