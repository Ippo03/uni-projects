import json

from rag.embedder import Embedder
from rag.retriever import Retriever
from rag.generator import MistralGenerator
from rag.loader import load_chunks

# chunks = load_chunks()
# with open("dsv_chunks.json", "w", encoding="utf-8") as f:
#     json.dump(chunks, f, ensure_ascii=False, indent=2)

# print(f"Saved {len(chunks)} chunks to dsv_chunks.json")

# load them from the .json file
with open("dsv_chunks.json", "r", encoding="utf-8") as f:
    chunks = json.load(f)

print(f"Loaded {len(chunks)} chunks from dsv_chunks.json")

embedder = Embedder()
embeddings = embedder.embed_chunks(chunks)
print(f"Generated {len(embeddings)} embeddings")

builder = Retriever()
retriever = builder.build_retriever(chunks, embeddings)

# Define the query
query = "What courses are offered?"

# Retrieve relevant documents
docs = retriever.get_relevant_documents(query)
print(f"Retrieved {len(docs)} documents")

# Combine content from retrieved docs into context
context = "\n\n".join(doc.page_content for doc in docs)

# Load and use Mistral to generate a response
generator = MistralGenerator()
response = generator.generate_response(query=query, context=context)

# Display the result
print("\n--- Generated Response ---")
print(response)




