#pragma once
#include "Widget.h"
#include "graphics.h"

class Button : public Widget
{
};

class ClearFilterButton : public Button
{
public:
	void update() override;
	void draw() override;
	void init() override;

};

class GenreButton : public Button
{
public:
	void update() override;
	void draw() override;
	void init() override;
};

class ArrowButton : public Button
{
public:
	void update() override;
	void draw() override;
	void init() override;
	bool containsor(float x, float y);	
};