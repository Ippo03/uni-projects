from transformers import (
    AutoModelForCausalLM,
    AutoTokenizer,
    BitsAndBytesConfig,
    pipeline
)
import torch
import re


class MistralGenerator:
    def __init__(self, model_name="mistralai/Mistral-7B-Instruct-v0.3", device="cuda"):
        """
        Loads the Mistral model and tokenizer with quantization.
        """
        self.model_name = model_name
        self.device = device

        # Quantization config
        bnb_config = BitsAndBytesConfig(
            load_in_4bit=True,
            bnb_4bit_use_double_quant=True,
            bnb_4bit_quant_type="nf4",
            bnb_4bit_compute_dtype=torch.bfloat16
        )

        self.tokenizer = AutoTokenizer.from_pretrained(model_name, trust_remote_code=True)
        self.tokenizer.pad_token = self.tokenizer.eos_token
        self.tokenizer.padding_side = "right"

        self.model = AutoModelForCausalLM.from_pretrained(
            model_name,
            quantization_config=bnb_config,
            torch_dtype=torch.bfloat16,
            device_map=device
        )

        print(f"Mistral model loaded — memory used: {round(self.model.get_memory_footprint()/1024/1024/1024, 2)} GB")

    def generate_response(self, query: str, context: str = "", max_new_tokens=718) -> str:
        """
        Generate a response from Mistral given a user query and context.

        Args:
            query (str): The user question
            context (str): Optional RAG context (retrieved info)
            max_new_tokens (int): Max length of the response

        Returns:
            str: Generated response
        """
        prompt = f"<s>[INST] {context}\n\n{query} [/INST]"
        inputs = self.tokenizer(prompt, return_tensors="pt").to(self.model.device)
        outputs = self.model.generate(
            **inputs,
            max_new_tokens=max_new_tokens,
            do_sample=True,
            temperature=0.7,
            top_k=50,
            top_p=0.95,
            pad_token_id=self.tokenizer.pad_token_id
        )
        decoded = self.tokenizer.decode(outputs[0], skip_special_tokens=True)
        return decoded.split("[/INST]")[-1].strip()
