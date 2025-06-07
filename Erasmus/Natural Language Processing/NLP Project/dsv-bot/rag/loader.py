from urllib.parse import urljoin, urlparse

import requests
from bs4 import BeautifulSoup

# Default headers
HEADERS = {
    'User-Agent': (
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) '
        'AppleWebKit/537.36 (KHTML, like Gecko) '
        'Chrome/120.0.0.0 Safari/537.36'
    )
}

BASE_URL = "https://www.su.se/department-of-computer-and-systems-sciences/"
COURSE_URL = "https://www.su.se/english/search-courses-and-programmes"

def is_within_domain(url, base_url, course_url):
    return (urlparse(url).netloc == urlparse(base_url).netloc or
            urlparse(url).netloc == urlparse(course_url).netloc)

def scrape_page(url):
    try:
        response = requests.get(url, headers=HEADERS, timeout=10)
        response.raise_for_status()
        soup = BeautifulSoup(response.content, 'html.parser')
        text_elements = soup.find_all(['p', 'div'], text=True)
        text = " ".join(el.get_text(strip=True) for el in text_elements)
        return text
    except Exception as e:
        print(f"[ERROR] Failed to scrape {url}: {e}")
        return None

def find_sub_urls(url, visited):
    sub_urls = set()
    try:
        response = requests.get(url, headers=HEADERS, timeout=10)
        response.raise_for_status()
        soup = BeautifulSoup(response.content, 'html.parser')
        for link in soup.find_all('a', href=True):
            absolute_url = urljoin(url, link['href'])
            if "#" in absolute_url:
                absolute_url = absolute_url.split("#")[0]
            if (is_within_domain(absolute_url, BASE_URL, COURSE_URL) and
                absolute_url not in visited and
                (BASE_URL in absolute_url or COURSE_URL in absolute_url)):
                sub_urls.add(absolute_url)
    except Exception as e:
        print(f"[ERROR] Failed to find sub-urls in {url}: {e}")
    return sub_urls

def crawl_website(start_url):
    visited = set()
    queue = [start_url]
    data = []

    while queue:
        current_url = queue.pop(0)
        if current_url in visited:
            continue
        print(f"[SCRAPING] {current_url}")
        visited.add(current_url)

        text_content = scrape_page(current_url)
        if text_content:
            data.append({'url': current_url, 'content': text_content})

        sub_urls = find_sub_urls(current_url, visited)
        queue.extend(sub_urls - visited)

    return data

def load_chunks(base_url=BASE_URL) -> list[dict]:
    """
    Scrapes the DSV website and returns chunks, one per page.
    Each chunk is a dict with 'url' and 'content'.
    """
    return crawl_website(base_url)
