# search-ai
## AI enabled Notes Search and Management System

This is a Spring Boot application with Elasticsearch integration to manage and search notes. The application supports features like storing notes, searching notes by title/content, and advanced search options such as prefix-based matching (autocomplete) and fuzzy search. It also integrates with OpenAI for summarization of notes.

### Features
Store Notes: Create, update, and retrieve notes with title, content, and associated vector embeddings.

Search Notes: Perform text-based searches on note titles and content with fuzzy matching for better relevance.

Autocomplete: Implement prefix-based search for fast and relevant suggestions.

Summarization: Use OpenAI's GPT model to generate summaries of notes, storing the top 3 key points for each note.

Integration with Elasticsearch: Store and index notes for fast retrieval and search capabilities.

### Requirements
Java 17 (or higher)

Spring Boot 3.x

Elasticsearch 8.x (or a compatible version)

OpenAI API key (if using OpenAI integration for summarization)

### Setup and Installation
1. Clone the repository
   git clone https://github.com/yourusername/notes-search-app.git
   cd notes-search-app
2. Configure Elasticsearch
   Ensure Elasticsearch is running on your local machine or use a cloud-based Elasticsearch service. You can download and start Elasticsearch from here.

3. Configure application.properties
   In the src/main/resources/application.properties file, configure your Elasticsearch server:

spring.data.elasticsearch.client.reactive.endpoints=localhost:9200
spring.data.elasticsearch.index-prefix=notes
spring.elasticsearch.rest.uris=http://localhost:9200

If you're using OpenAI for summarization, add your API key as well:
openai.api.key=your-openai-api-key

5. Build and run the application
   Build the application using Maven:

mvn clean install

Run the application:

mvn spring-boot:run

6. Access the API
   Once the application is running, you can access the API endpoints.

Create a new note: POST /notes

Retrieve all notes: GET /notes

Search notes by text: GET /notes/search?keyword={keyword}

Summarize a note: POST /notes/summarize

### API Endpoints
1. Create a Note
   Endpoint: POST /notes

Request body:

{
"title": "Note Title",
"content": "This is the content of the note."
}
2. Get All Notes
   Endpoint: GET /notes

Response:

[
{
"id": "1",
"title": "Note Title",
"content": "This is the content of the note.",
"createdAt": "2025-05-10T00:00:00Z",
"updatedAt": "2025-05-10T00:00:00Z"
}
]

3. Search Notes by Keyword
   Endpoint: GET /notes/search?keyword={keyword}

Description: Search notes by title and content, supporting fuzzy matching.

Example: GET /notes/search?keyword=note

4. Summarize a Note
   Endpoint: POST /notes/summarize

Request body:

{
"id": "1",
"content": "This is the content that will be summarized by GPT."
}
5. Autocomplete Search
   Endpoint: GET /notes/autocomplete?prefix={prefix}

Description: Get prefix-based search results for note titles.

Example: GET /notes/autocomplete?prefix=note

### Elasticsearch Integration
This application uses Elasticsearch for storing and searching notes. The search is performed on the title and content fields, utilizing Elasticsearch's prefix matching and fuzzy search for relevant results.

### Index Configuration
The application configures the index with custom analyzers to handle autocomplete and fuzzy search functionality efficiently.


### OpenAI Integration
If enabled, the application uses OpenAI's GPT models to summarize the content of notes, highlighting the top 3 key points. This is helpful for users who want a concise summary of lengthy notes.

API Key: Make sure to set your OpenAI API key in the application.properties to enable this feature.

### Technologies Used
Spring Boot: Framework for building the application.

Elasticsearch: Search and indexing engine for efficient note search.

OpenAI: Used for summarizing notes using GPT.

Java 17: Programming language for developing the application.

### Troubleshooting
Elasticsearch Not Found:
Ensure Elasticsearch is running on the configured URL (localhost:9200 or any other endpoint you've set).

OpenAI API Key Missing:
Ensure that the OpenAI API key is correctly added to the application.properties file.

Search Issues:
If search results are not relevant, consider adjusting the analyzer configuration in Elasticsearch for better matching.

### License
This project is licensed under the MIT License - see the LICENSE file for details.

This README should provide users with all the necessary information to get started with your application. Let me know if you need any changes!