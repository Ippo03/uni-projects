from sentence_transformers import SentenceTransformer


class Embedder:
    def __init__(self, model_name="sentence-transformers/paraphrase-mpnet-base-v2"):
        """
        Initializes the sentence transformer model for embedding text.
        """
        self.model = SentenceTransformer(model_name)

    def embed_chunks(self, chunks: list[dict]) -> list:
        """
        Takes a list of chunks (dicts with 'content') and returns their embeddings.
        
        Args:
            chunks (list of dict): Each dict must have a 'content' field.

        Returns:
            list: Embeddings corresponding to the input chunks.
        """
        texts = [chunk["content"] for chunk in chunks if "content" in chunk]
        embeddings = self.model.encode(texts, convert_to_tensor=False)
        return embeddings
