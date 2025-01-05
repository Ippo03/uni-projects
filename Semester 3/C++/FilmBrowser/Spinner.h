#pragma once
#include "Widget.h"
#include "Film.h"

class Spinner : public Widget
{
public:
	void update() override;
	void draw() override;
	void init() override;
	void spin();
	void findActiveFilm(std::list<Film*> l);
	void draw_init(std::list<Film*> l);
	void draw_search(std::list< Film*> l);
	Spinner();
	~Spinner();
};