#pragma once
#include "Widget.h"	
#include "Dock.h"
#include "Button.h"
#include "Line.h"
#include "Spinner.h"
#include "Film.h"
#include "TextField.h"
#include <list>

class Browser 
{
public:
	void update();
	void draw();
	void init();
	//constructor
	Browser();
	//destructor
	~Browser();
	//states
	enum browser_state { STATE_INIT, STATE_SEARCH_GENRE, STATE_SEARCH_YEAR, STATE_SEARCH_TEXT, STATE_SEARCH};
protected:
	//list of all widgets
	std::list<Widget *> m_widgets;
	//list of all genre buttons
	std::list<GenreButton*> genreButtons;
	//at start state = init
	browser_state m_state = STATE_INIT;
	//pointers to all widgets
	Dock* dock = nullptr;
	ClearFilterButton* clearfilterbutton = nullptr;
	TextFieldTitle* tftitle = nullptr;
	TextFieldActor* tfactor = nullptr;
	TextFieldDirector* tfdirector = nullptr;
	LineFrom* linefrom = nullptr;
	LineTo* lineto = nullptr;
	Spinner* spinner = nullptr;
	ArrowButton* arrowbutton = nullptr;
};
