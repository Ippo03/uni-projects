#pragma once
#include "Widget.h"
#include "graphics.h"

class TextField : public Widget
{
public:
	//method that writes on the field
	std::string write();
};

class TextFieldTitle : public TextField
{
public:
	void update() override;
	void draw() override;
	void init() override;
};

class TextFieldActor : public TextField
{
public:
	void update() override;
	void draw() override;
	void init() override;
};

class TextFieldDirector : public TextField
{
public:
	void update() override;
	void draw() override;
	void init() override;
};