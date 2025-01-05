#pragma once
#include "Widget.h"
#include "defines.h"
#include "graphics.h"

class Dock : public Widget
{
public:
	void update() override;
	void draw() override;
	void init() override;
};