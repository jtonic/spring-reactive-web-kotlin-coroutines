# Kafka Development Environment

This Docker Compose setup provides a local Kafka development environment with the following components:

- **ZooKeeper**: Required for Kafka (port 2181)
- **Kafka**: Message broker with both internal and external listeners (ports 9092, 9093)
- **Kafka UI**: Web interface for managing Kafka (port 8080)

Optional components (commented out by default):
- **Schema Registry**: For managing Avro schemas (port 8081)
- **Kafka Connect**: For data integration with other systems (port 8083)

## Usage

### Starting the environment
