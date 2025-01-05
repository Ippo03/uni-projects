#include "Browser.h"
#include "graphics.h"
#include "defines.h"

void Browser::update()
{
	graphics::MouseState ms;
	graphics::getMouseState(ms);

	float mx = graphics::windowToCanvasX(ms.cur_pos_x);
	float my = graphics::windowToCanvasY(ms.cur_pos_y);

	//update widgets
	for (auto widget : m_widgets)
		widget->update();

	//highlights mouse coordinates
	for (auto widget : m_widgets)
		widget->setHighlighted(widget->contains(mx, my));

	//highlights dock	
	for (auto widget : m_widgets)
		widget->setDockHighlighted(dock->contains(mx, my));

	//changes subimages
	if (ms.button_left_pressed && arrowbutton->containsor(mx, my))
	{
		spinner->setClicked(true);
	}
	else
	{
		spinner->setClicked(false);
	}

	//search
	//signal genre search
	for (auto genre : genreButtons)
	{
		if (ms.button_left_pressed && genre->contains(mx, my))
		{
			m_state = STATE_SEARCH_GENRE;
			spinner->GenreSearching(true);
			spinner->setActiveGenre(genre->getLabel());
			spinner->setSpinnerSpeed(2.0f);
		}
	}

	//signal year search
	if (ms.dragging && (linefrom->contains(mx, my) || lineto->contains(mx, my)))
	{
		m_state = STATE_SEARCH_YEAR;
		spinner->YearSearching(true);
		spinner->setActiveYearFrom(linefrom->getYearFrom());
		spinner->setActiveYearTo(lineto->getYearTo());
		spinner->setSpinnerSpeed(2.0f);
	}

	//signal text search
	if (tftitle->Writing() || tfdirector->Writing() || tfactor->Writing())
	{
		m_state = STATE_SEARCH_TEXT;
		spinner->TextSearching(true);
		spinner->setActiveTextTitle(tftitle->getSearchTitle());
		spinner->setActiveTextActor(tfactor->getSearchActors());
		spinner->setActiveTextDirector(tfdirector->getSearchDirectors());
		spinner->setSpinnerSpeed(2.0f);
	}

	//state = search if use more that one search
	if (m_state == STATE_SEARCH_GENRE || m_state == STATE_SEARCH_TEXT || m_state == STATE_SEARCH_YEAR) 
	{
		m_state = STATE_SEARCH;
		spinner->setActive(true);
		spinner->setCounter(0);
	}

	//clear all filters
	if (ms.button_left_pressed && clearfilterbutton->contains(mx, my))
	{
		m_state = STATE_INIT;
		spinner->GenreSearching(false);
		spinner->YearSearching(false);
		linefrom->ZeroYearFrom();
		linefrom->setPosX(250.f);
		linefrom->setPosY(150.f);
		linefrom->setStartX(linefrom->getPosX() - 4.f);
		linefrom->setEndX(linefrom->getPosX() + 4.f);
		lineto->ZeroYearTo();
		lineto->setPosX(400.f);
		lineto->setPosY(190.f);
		lineto->setStartX(lineto->getPosX() - 4.f);
		lineto->setEndX(lineto->getPosX() + 4.f);
		spinner->TextSearching(false);
		tftitle->ClearTextFieldTitle();
		tfdirector->ClearTextFieldDirector();
		tfactor->ClearTextFieldActors();
		spinner->ClearGenreList();
		spinner->ClearYearList();
		spinner->ClearTextList();
		spinner->ClearCommon2List();
		spinner->ClearCommon3List();
		spinner->setSpinnerSpeed(4.f);
		spinner->setActive(true);
		spinner->setCounter(0);
	}
}

void Browser::draw()
{
	//draw background
	graphics::Brush br;
	br.outline_opacity = 0.0f;
	br.texture = std::string(ASSET_PATH) + "cinema.png";
	graphics::drawRect(CANVAS_WIDTH / 2, CANVAS_HEIGTH / 2, CANVAS_WIDTH, CANVAS_WIDTH - 200, br);

	//draw texts
	graphics::Brush br1;
	graphics::drawText(CANVAS_WIDTH / 2 - 475, CANVAS_HEIGTH - 120, 17.f, "Director:", br1);
	graphics::drawText(CANVAS_WIDTH / 2 - 475, CANVAS_HEIGTH - 100, 17.f, "Stars:", br1);
	graphics::drawText(CANVAS_WIDTH / 2 - 475, CANVAS_HEIGTH - 80, 17.f, "Year:", br1);
	graphics::drawText(CANVAS_WIDTH / 2 - 475, CANVAS_HEIGTH - 40, 17.f, "Summary:", br1);

	//draw widgets for every state
	if (m_state ==  STATE_INIT)
	{
		for (auto widget : m_widgets)
			widget->draw();
	}

	if (m_state == STATE_SEARCH)
	{
		for (auto widget : m_widgets)
		{
			widget->draw();
		}
	}
}

void Browser::init()
{
	//font
	graphics::setFont(std::string(ASSET_PATH) + "MossionDemo.otf");

	//add every widget to the widget list
	//initializes dock
	dock = new Dock();
	dock->setPosX(CANVAS_WIDTH / 2);
	dock->setPosY(-110.f);
	dock->setStartX(200.f);
	dock->setEndX(800.f);
	dock->setStartY(0.f);
	dock->setEndY(dock->getVarY());
	m_widgets.push_back(dock);

	//initializes clearfilterbutton
	clearfilterbutton = new ClearFilterButton();
	clearfilterbutton->setPosX(CANVAS_WIDTH / 2 + 150.f);
	clearfilterbutton->setPosY(30.0f);
	clearfilterbutton->setStartX(575.f);
	clearfilterbutton->setEndX(725.f);
	clearfilterbutton->setStartY(15.f);
	clearfilterbutton->setEndY(40.f);
	clearfilterbutton->setLabel("Clear Filters");
	m_widgets.push_back(clearfilterbutton);

	//initializes textfield for title
	tftitle = new TextFieldTitle();
	tftitle->setPosX(650.f);
	tftitle->setPosY(140.f);
	tftitle->setStartX(540.f);
	tftitle->setEndX(760.f);
	tftitle->setStartY(120.f);
	tftitle->setEndY(140.f);
	m_widgets.push_back(tftitle);

	//initializes textfield for actors
	tfactor = new TextFieldActor();
	tfactor->setPosX(650.f);
	tfactor->setPosY(165.f);
	tfactor->setStartX(540.f);
	tfactor->setEndX(760.f);
	tfactor->setStartY(145.f);
	tfactor->setEndY(165.f);
	m_widgets.push_back(tfactor);

	//initializes textfield for directors
	tfdirector = new TextFieldDirector();
	tfdirector->setPosX(650.f);
	tfdirector->setPosY(190.f);
	tfdirector->setStartX(540.f);
	tfdirector->setEndX(760.f);
	tfdirector->setStartY(170.f);
	tfdirector->setEndY(190.f);
	m_widgets.push_back(tfdirector);

	//initialize line from
	linefrom = new LineFrom();
	linefrom->setPosX1(250.f);
	linefrom->setPosY1(150.f);
	linefrom->setPosX2(400.f);
	linefrom->setPosY2(150.f);
	linefrom->setPosX(250.f);
	linefrom->setPosY(150.f);
	linefrom->setStartX(linefrom->getPosX() - 4.f);
	linefrom->setEndX(linefrom->getPosX() + 4.f);
	linefrom->setStartY(linefrom->getPosY() - 6.f);
	linefrom->setEndY(linefrom->getPosY() + 6.f);
	m_widgets.push_back(linefrom);

	//initialize line to
	lineto = new LineTo();
	lineto->setPosX1(250.f);
	lineto->setPosY1(190.f);
	lineto->setPosX2(400.f);
	lineto->setPosY2(190.f);
	lineto->setPosX(400.f);
	lineto->setPosY(190.f);
	lineto->setStartX(lineto->getPosX() - 4.f);
	lineto->setEndX(lineto->getPosX() + 4.f);
	lineto->setStartY(lineto->getPosY() - 6.f);
	lineto->setEndY(lineto->getPosY() + 6.f);
	m_widgets.push_back(lineto);

	//initialize genre buttons
	const int NUM_BUTTONS = 7;
	float posX = 240.f;
	float posY = 30.f;
	const char* labels[NUM_BUTTONS] = { "action", "adventure", "drama", "history", "romance", "comedy", "fantasy" };

	for (int i = 0; i < NUM_BUTTONS; ++i)
	{
		GenreButton * genreButton = new GenreButton();
		genreButton->setPosX(posX);
		genreButton->setPosY(posY);
		genreButton->setStartX(genreButton->getPosX() - 25.f);
		genreButton->setEndX(genreButton->getPosX() + 25.f);
		genreButton->setStartY(genreButton->getPosY() - 7.5f);
		genreButton->setEndY(genreButton->getPosY() + 7.5f);
		genreButton->setLabel(labels[i]);
		genreButtons.push_back(genreButton);
		m_widgets.push_back(genreButton);

		posX += 70.f;
		if (posX > 450.f)
		{
			posX = 240.f;
			posY += 25.f;
		}
	}
	
	//initialize arrow button
	arrowbutton = new ArrowButton();
	arrowbutton->setPosX1(CANVAS_WIDTH / 2 + 235.f);
	arrowbutton->setPosY1(CANVAS_HEIGTH - 125.f);
	arrowbutton->setPosX2(CANVAS_WIDTH / 2 + 465.f);
	arrowbutton->setPosY2(CANVAS_HEIGTH - 125.f);
	arrowbutton->setStartX(725.f);
	arrowbutton->setEndX(975.f);
	arrowbutton->setStartY(300.f);
	arrowbutton->setEndY(450.f);
	m_widgets.push_front(arrowbutton);

	//initialize spinner
	spinner = new Spinner();
	spinner->setStartX(55.f);
	spinner->setEndX(945.f);
	spinner->setStartY(40.f);
	spinner->setEndY(260.f);
	m_widgets.push_front(spinner);
}

Browser::Browser()
{
}

Browser::~Browser()
{
	//delete widgets
	for (auto widget : m_widgets)
		delete widget;
}

