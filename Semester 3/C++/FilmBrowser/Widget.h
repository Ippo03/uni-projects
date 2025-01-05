#pragma once
#include "defines.h"
#include <string>
#include <list>
#include "Film.h"

//abstract class
class Widget
{
protected:
	//public widget variables
	float pos_x;
	float pos_y;
	float startx;
	float starty;
	float endx;
	float endy;
	bool widget_highlighted = false;

	//dock variables
	float dock_speed = 5.0f;
	bool indock = false;
	bool onetime = false;
	float var_y = 15.0f;
	bool dock_highlighted = false;
	
	//button variables
	bool box_highlighted = false;
	bool arrow_highlighted = false;
	std::string label;
	
	//line variables
	float line_pos_x1;
	float line_pos_y1;
	float line_pos_x2;
	float line_pos_y2;
	int yearfrom = 1972;
	int yearto = 2007;
	float prevmx;

	//spinner variables
	float width = 170.f;
	float height = 220.f;
	float spinner_speed = 4.f;
	float pos_offset;
	bool clicked = false;
	bool first_time = true;
	bool has_active;
	int count;
	bool writing;

	//fieldtext variables
	std::string searchtitle = "";
	std::string searchactors = "";
	std::string searchdirectors = "";
	std::string s;

	//search variables
	std::string act_genre;
	std::string cur_genre;
	int act_year_from;
	int act_year_to;
	std::string act_title;
	std::string act_actors;
	std::string act_dir;
	std::string sologenre1;
	std::string sologenre2;
	bool genre_search = false;
	bool year_search = false;
	bool text_search = false;
	bool draw_search;

public:
	//virtual methods
	virtual void update() = 0;
	virtual void draw() = 0;
	virtual void init() = 0;

	//list of films
	std::list <Film*> m_films;
	//genre searh list
	std::list<Film*> genre_results;
	//year search list
	std::list<Film*> year_results;
	//text search list
	std::list<Film*> text_results;
	//combine search lists
	std::list<Film*> common3_results;
	std::list<Film*> common2_results;
	//pointer to active film
	Film* active_film = nullptr;
	//pointer to current film
	Film* current_film = nullptr;

	//public widget methods
	float getPosX() { return pos_x; }
	float getPosY() { return pos_y; }
	void setPosX(float x) { pos_x = x; }
	void setPosY(float y) { pos_y = y; }
	float getStartX() { return startx; }
	void setStartX(float sx) { startx = sx; }
	float getStartY() { return starty; }
	void setStartY(float sy) { starty = sy; }
	float getEndX() { return endx; }
	void setEndX(float ex) { endx = ex; }
	float getEndY() { return endy; }
	void setEndY(float ey) { endy = ey; }
	void setHighlighted(bool h) { widget_highlighted = h; }
	bool contains(float x, float y)
	{
		return (x >= startx && x <= endx && y >= starty && y <= endy);
	}

	//dock methods
	float getVarY() { return var_y; }
	void setVarY(float var) { var_y = var; }
	void setDockHighlighted(bool h) { dock_highlighted = h; }

	//line methods
	void setPosX1(float x1) { line_pos_x1 = x1; }
	float getPosX1() { return line_pos_x1; }
	void setPosX2(float x2) { line_pos_x2 = x2; }
	float getPosX2() { return line_pos_x2; }
	void setPosY1(float y1) { line_pos_y1 = y1; }
	float getPosY1() { return line_pos_y1; }
	void setPosY2(float y2) { line_pos_y2 = y2; }
	float getPosY2() { return line_pos_y2; }
	int getYearFrom() { return yearfrom; }
	int getYearTo() { return yearto; }
	void ZeroYearFrom() { yearfrom = 1972; }
	void ZeroYearTo() { yearto = 2007; }

	//button methods
	std::string getLabel() { return label; }
	void setLabel(std::string l) { label = l; }

	//spinner methods
	void setClicked(bool h) { clicked = h; }
	void setSpinnerSpeed(float sp) { spinner_speed = sp; }
	void setCounter(int c) { count = c; }
	void setActive(bool a) { has_active = a;}
	
	//fieldtext methods
	bool Writing() { return writing; }
	std::string getSearchTitle() { return searchtitle; }
	std::string getSearchActors() { return searchactors; }
	std::string getSearchDirectors() { return searchdirectors; }
	void ClearTextFieldTitle() { searchtitle = ""; }
	void ClearTextFieldActors() { searchactors = ""; }
	void ClearTextFieldDirector() { searchdirectors = ""; }

	//search methods
	void setActiveTextTitle(std::string actt) { act_title = actt; }
	void setActiveTextActor(std::string acta) { act_actors = acta; }
	void setActiveTextDirector(std::string actd) { act_dir = actd; }
	void setActiveGenre(std::string actg) {  act_genre = actg; }
	void setActiveYearFrom(int y) { act_year_from = y; }
	void setActiveYearTo(int y) { act_year_to = y; }
	bool isDifferent() { return act_genre != cur_genre; }
	void GenreSearching(bool h) { genre_search = h; }
	void YearSearching(bool h) { year_search = h; }
	void TextSearching(bool h) { text_search = h; }
	void ClearCommon2List() { common2_results.clear(); }
	void ClearCommon3List() { common3_results.clear(); }
	void ClearGenreList() { genre_results.clear(); }
	void ClearYearList() { year_results.clear(); }
	void ClearTextList() { text_results.clear(); }
};


