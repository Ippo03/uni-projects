from langchain_community.vectorstores import Chroma
from langchain_community.embeddings import HuggingFaceEmbeddings
from langchain.schema import Document
import os

class Retriever:
    def __init__(self, persist_directory="chroma_index"):
        """
        Initializes the retriever with a local Chroma vectorstore.
        """
        self.persist_directory = persist_directory
        self.vectorstore = None

    def build_retriever(self, chunks, embeddings) -> Chroma.as_retriever:
        """
        Builds a retriever from given chunks and their precomputed embeddings.

        Args:
            chunks (list of dict): Each dict must have 'content' and 'url'
            embeddings (list): Embedding vectors matching the chunks

        Returns:
            A retriever instance (Chroma retriever)
        """
        documents = []
        metadatas = []

        for chunk in chunks:
            content = chunk.get("content", "")
            url = chunk.get("url", "")
            doc = Document(page_content=content, metadata={"source": url})
            documents.append(doc)
            metadatas.append({"source": url})

        # Use HuggingFaceEmbeddings wrapper (required by Chroma)
        embedding_function = HuggingFaceEmbeddings(model_name="sentence-transformers/paraphrase-mpnet-base-v2")

        self.vectorstore = Chroma.from_documents(
            documents=documents,
            embedding=embedding_function,
            persist_directory=self.persist_directory
        )
        return self.vectorstore.as_retriever()
