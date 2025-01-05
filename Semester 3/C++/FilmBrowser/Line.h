#pragma once
#include "Widget.h"

class Line : public Widget
{
};

class LineFrom : public Line
{
public:
	void update() override;
	void draw() override;
	void init() override;
};

class LineTo : public Line 
{
public:
	void update() override;
	void draw() override;
	void init() override;
};
