#include "Spinner.h"
#include "graphics.h"

void Spinner::update()
{
	graphics::MouseState ms;
	graphics::getMouseState(ms);

	float mx = graphics::windowToCanvasX(ms.cur_pos_x);
	float my = graphics::windowToCanvasY(ms.cur_pos_y);

	//sets active the first film of the list in init state
	if (first_time)
	{
		active_film = m_films.front();
		first_time = false;
	}

	//changes subimages of the active film
	for (auto film : m_films)
	{
		if (clicked)
		{
			film->setSubCount(film->getSubCount() + 1);
		}
	}

	//finds active film in init state
	if (!text_search && !genre_search && !year_search)
	{
		findActiveFilm(m_films);
	}

	//search with textfields(add in list if input matches with title & actors & director of active film)
	if (text_search)
	{
		for (auto film : m_films)
		{
			//we check if substrings are a part of the string
			if ((film->getTitle().find(act_title) != std::string::npos) && (film->getActors().find(act_actors) != std::string::npos) && (film->getDirector().find(act_dir) != std::string::npos))
			{
				text_results.push_back(film);
				//finds active film in text results
				if (!genre_search && !year_search)
					findActiveFilm(text_results);
			}
			else
			{
				text_results.remove(film);
			}
		}
	}

	//search with year(add in list if year of active film is in year from & year to boundary)
	if (year_search)
	{
		for (auto film : m_films)
		{
			if (film->getYear() >= act_year_from && film->getYear() <= act_year_to)
			{
				year_results.push_back(film);
				//finds active film in year results
				if(!genre_search && !text_search)
					findActiveFilm(year_results);
			}
			if (film->getYear() < act_year_from || film->getYear() > act_year_to)
			{
				year_results.remove(film);
			}
		}
	}

	//search with genre(add in list if genre of active film matches genre clicked)
	if (genre_search)
	{
		if (isDifferent())
		{
			genre_results.clear();
		}
		for (auto film : m_films)
		{
			//have a string with ',' and split it into two new strings
			s = film->getGenre();
			size_t pos = s.find(", ");  
			sologenre1 = s.substr(0, pos);  
			sologenre2 = s.substr(pos + 2);
			if (sologenre1 == act_genre || sologenre2 == act_genre)
			{
				genre_results.push_back(film);
				//finds active film in genre results
				if (!year_search && !text_search)
					findActiveFilm(genre_results);
			}
			cur_genre = act_genre;
		}
	}

	//use all searches(check if film is inside both of 3 lists)
	if (year_search && genre_search && text_search)
	{
		bool has_active = true;
		for (auto film : m_films)
		{
			if (std::count(year_results.begin(), year_results.end(), film) > 0)
			{
				if (std::count(text_results.begin(), text_results.end(), film) > 0)
				{
					if (std::count(genre_results.begin(), genre_results.end(), film) > 0)
					{
						common3_results.push_back(film);
						//finds active film in common 3 results
						findActiveFilm(common3_results);
					}
					else
					{
						common3_results.remove(film);
					}
				}
				else
				{
					common3_results.remove(film);
				}
			}
			else
			{
				common3_results.remove(film);
			}
		}
	}
	//use two searches(3 cases every time check if film is inside both of 2 lists)
	if (text_search && genre_search)
	{
		bool has_active = true;
		for (auto film : m_films)
		{
			if (std::count(text_results.begin(), text_results.end(), film) > 0)
			{
				if (std::count(genre_results.begin(), genre_results.end(), film) > 0)
				{
					common2_results.push_back(film);
					//finds active film in common 2 results
					findActiveFilm(common2_results);
				}
				else
				{
					common2_results.remove(film);
				}
			}
			else
			{
				common2_results.remove(film);
			}
		}
	}
	else if (year_search && genre_search)
	{
		bool has_active = true;
		for (auto film : m_films)
		{
			if (std::count(year_results.begin(), year_results.end(), film) > 0)
			{
				if (std::count(genre_results.begin(), genre_results.end(), film) > 0)
				{
					common2_results.push_back(film);
					//finds active film in common 2 results
					findActiveFilm(common2_results);
				}
				else
				{
					common2_results.remove(film);
				}
			}
			else
			{
				common2_results.remove(film);
			}
		}
	}
	else if (year_search && text_search)
	{
		bool has_active = true;
		for (auto film : m_films)
		{
			if (std::count(year_results.begin(), year_results.end(), film) > 0)
			{
				if (std::count(text_results.begin(), text_results.end(), film) > 0)
				{
					common2_results.push_back(film);
					//finds active film in common 2 results
					findActiveFilm(common2_results);
				}
				else
				{
					common2_results.remove(film);
				}
			}
			else
			{
				common2_results.remove(film);
			}
		}
	}

	//move spinner
	if (ms.dragging && contains(mx, my) && !dock_highlighted)
	{
		pos_offset = spinner_speed * graphics::getDeltaTime() / 20.f;
		spin();
	}
}

void Spinner::draw()
{
	//no search, draws init state, all films
	if (!genre_search && !year_search && !text_search)
	{
		draw_init(m_films);
	}
	//only draws films that are searched
	else if (year_search && genre_search && text_search)
	{
		draw_search(common3_results);
	}
	else if ((text_search && year_search) || (year_search && genre_search) || (genre_search && text_search))
	{
		draw_search(common2_results);
	}
	else if (year_search && !text_search && !genre_search)
	{
		draw_search(year_results);
	}
	else if (genre_search && !year_search && !text_search)
	{
		draw_search(genre_results);
	}
	else if (text_search && !genre_search && !year_search)
	{
		draw_search(text_results);
	}

	//draw info of active film
	graphics::Brush br4;
	graphics::drawText(CANVAS_WIDTH / 2 - 475.f, CANVAS_HEIGTH - 160.f, 25.f, active_film->getTitle(), br4);
	graphics::drawText(CANVAS_WIDTH / 2 - 415.f, CANVAS_HEIGTH - 120.f, 17.f, active_film->getDirector(), br4);
	graphics::drawText(CANVAS_WIDTH / 2 - 415.f, CANVAS_HEIGTH - 100.f, 17.f, active_film->getActors(), br4);
	graphics::drawText(CANVAS_WIDTH / 2 - 415.f, CANVAS_HEIGTH - 80.f, 17.f, std::to_string(active_film->getYear()), br4);
	graphics::drawText(CANVAS_WIDTH / 2 - 410.f, CANVAS_HEIGTH - 40.f, 17.f, active_film->getSummary(), br4);
	graphics::drawText(CANVAS_WIDTH / 2 - 475.f, CANVAS_HEIGTH - 20.f, 17.f, active_film->getSummary1(), br4);
}

void Spinner::init()
{
	//font
	graphics::setFont(std::string(ASSET_PATH) + "MossionDemo.otf");
}

bool has_active = true;
//method that highlights active film
void Spinner::findActiveFilm(std::list< Film*> l)
{
	for (auto film : l)
	{
		current_film = film;
		//change to active to the center one
		if (film->getPosX() >= 450.f && film->getPosX() <= 550.f && film->getDepth() == 1.0f && current_film)
		{
			if (active_film != current_film)
			{
				graphics::playSound(std::string(ASSET_PATH) + "browse.wav", 0.7f, 0);
			}
			active_film = current_film;
		}
		//first time make active the film in the front of the list
		else if (has_active)
		{
			active_film = l.front();
			has_active = false;
		}
	}
}

//method that spins the spinner
void Spinner::spin()
{
	//films have boundaries(140 & 860), when they reach them ,their depth changes
	for (auto film : m_films)
	{
		if (film->isFront())
		{
			if (film->getPosX() - pos_offset >= 140.f)
			{
				film->setPosX(film->getPosX() - pos_offset);
			}
			else
			{
				film->setPosX(140.f);
				film->setDepth(0.0f);
			}
		}
		if (film->isNotFront())
		{
			if (film->getPosX() + pos_offset <= 860.f)
			{
				film->setPosX(film->getPosX() + pos_offset);
			}
			else
			{
				film->setPosX(860.f);
				film->setDepth(1.0f);
			}
		}
	}
}

//method that draws all films
void Spinner::draw_init(std::list< Film*> l)
{
	float h = 0.5f + 0.5f * sinf(graphics::getGlobalTime() / 100.f);
	graphics::Brush br;
	for (auto film : m_films)
	{
		//only draws films in front
		if (film->isFront())
		{
			br.outline_opacity = 0.0f;
			br.outline_width = 4.0f;
			br.texture = std::string(ASSET_PATH) + film->getImage();
			//active->highlighting outline
			if (film == active_film)
			{
				br.outline_opacity = 0.7f * h;
			}
			//middle->full opacity
			if (film->getPosX() >= 320 && film->getPosX() <= 680)
			{
				br.fill_opacity = 1.f;
			}
			//edges->lower opacity
			else if (film->getPosX() >= 140 && film->getPosX() <= 280 || film->getPosX() >= 720 && film->getPosX() <= 860)
			{
				br.fill_opacity = 0.75f;
			}
			graphics::drawRect(film->getPosX(), film->getPosY(), width, height, br);

			//shadow
			graphics::Brush br1;
			graphics::setOrientation(-180.f);
			br1.fill_opacity = 0.2f;
			br1.texture = std::string(ASSET_PATH) + film->getImage();
			br1.outline_opacity = 0.0f;
			graphics::drawRect(film->getPosX(), film->getPosY() + 190.f, width, height - 30.f, br1);
			graphics::resetPose();

			//clicked then changes subimages
			graphics::Brush br2;
			br2.texture = std::string(ASSET_PATH) + active_film->getSubImage1();
			if (film->getSubCount() % 2 == 0)
			{
				br2.texture = std::string(ASSET_PATH) + active_film->getSubImage1();
			}
			else if (film->getSubCount() % 2 != 0)
			{
				br2.texture = std::string(ASSET_PATH) + active_film->getSubImage2();
			}
			graphics::drawRect(CANVAS_WIDTH / 2 + 350.f, CANVAS_HEIGTH - 125.f, 250.f, 150.f, br2);
		}
	}
}

//method that draws only searched films
void Spinner::draw_search(std::list< Film*> l)
{
	float h1 = 0.5f + 0.5f * sinf(graphics::getGlobalTime() / 100.f);
	//draws all films on the list with full opacity
	for (auto film : l)
	{
		graphics::Brush br3;
		br3.outline_opacity = 0.0f;
		br3.outline_width = 4.0f;
		//active->highlighting outline
		if (film == active_film)
		{
			br3.outline_opacity = 0.7f * h1;
		}
		br3.fill_opacity = 1.f;
		br3.texture = std::string(ASSET_PATH) + film->getImage();
		graphics::drawRect(film->getPosX(), film->getPosY(), width, height, br3);

		//clicked then changes subimages
		graphics::Brush br2;
		br2.texture = std::string(ASSET_PATH) + active_film->getSubImage1();
		if (film->getSubCount() % 2 == 0)
		{
			br2.texture = std::string(ASSET_PATH) + active_film->getSubImage1();
		}
		else if (film->getSubCount() % 2 != 0)
		{
			br2.texture = std::string(ASSET_PATH) + active_film->getSubImage2();
		}
		graphics::drawRect(CANVAS_WIDTH / 2 + 350.f, CANVAS_HEIGTH - 125.f, 250.f, 150.f, br2);
	}
}

Spinner::Spinner()
{
	//creates array of every film info
	const int NUM_FILMS = 8;
	const std::string titles[NUM_FILMS] = { t0, t1, t2, t3, t4, t5, t6, t7 };
	const int years[NUM_FILMS] = { ye0, ye1, ye2, ye3, ye4, ye5, ye6, ye7 };
	const std::string directors[NUM_FILMS] = { d0, d1, d2, d3, d4, d5, d6, d7 };
	const std::string actors[NUM_FILMS] = { a0, a1, a2, a3, a4, a5, a6, a7 };
	const std::string genres[NUM_FILMS] = { g0, g1, g2, g3, g4, g5, g6 ,g7 };
	const std::string summaries[NUM_FILMS] = { s0, s1, s2, s3, s4, s5, s6, s7 };
	const std::string summary1s[NUM_FILMS] = { s00, s11, s21, s31, s41, s51, s61, s71 };
	const float posXs[NUM_FILMS] = { 500.f, 680.f, 320.f, 860.f, 140.f, 680.f, 500.f, 320.f };
	const float posYs[NUM_FILMS] = { 150.f, 150.f, 150.f, 150.f, 150.f, 150.f, 150.f, 150.f };
	const std::string images[NUM_FILMS] = { "harrypotter1.png", "homealone2.png", "titanic.png", "godfather.png", "greenmile.png", "harrypotter5.png", "indianajones.png", "karatekid.png" };
	const std::string subImages1[NUM_FILMS] = { "11.png", "21.png", "31.png", "41.png", "51.png", "61.png", "71.png", "81.png" };
	const std::string subImages2[NUM_FILMS] = { "12.png", "22.png", "32.png", "42.png", "52.png", "62.png", "72.png", "82.png" };

	//initaliazes films using for loop
	for (int i = 0; i < NUM_FILMS; ++i)
	{
		Film* film = new Film();
		m_films.push_back(film);
		film->setTitle(titles[i]);
		film->setYear(years[i]);
		film->setActors(actors[i]);
		film->setDirector(directors[i]);
		film->setGenre(genres[i]);
		film->setSummary(summaries[i]);
		film->setSummary1(summary1s[i]);
		film->setImage(images[i]);
		film->setSubImage1(subImages1[i]);
		film->setSubImage2(subImages2[i]);
		film->setSubCount(0);
		film->setPosX(posXs[i]);
		film->setPosY(posYs[i]);
		film->setDepth(1.0f);
		if (i >= 5)
		{
			film->setDepth(0.0f);
		}
	}
	//initializes active film
	active_film = new Film();
}

Spinner::~Spinner()
{
	//delete films
	for (auto film : m_films)
	{
		delete film;
	}

	//delete active film
	if (active_film)
		delete active_film;
}