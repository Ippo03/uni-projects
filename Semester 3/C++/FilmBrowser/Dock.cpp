#include "Dock.h"

void Dock::update()
{
	//moves down dock according to offset when highlighted
	float offset;
	if (widget_highlighted && pos_y < DOCK_HEIGHT - 128.f)
	{
		pos_y += dock_speed * graphics::getDeltaTime() / 10.0f;
		offset = dock_speed * graphics::getDeltaTime() / 10.0f;
		setVarY(getVarY() + offset);
		setEndY(getVarY());

		if (indock && onetime && getVarY() >= DOCK_HEIGHT - 15.f)
		{
			graphics::playSound(std::string(ASSET_PATH) + "maximize.wav", 0.7f, 0);
			onetime = false;
		}
		indock = true;
	}
	//takes dock to starting point
	else if (!widget_highlighted && getVarY() > 15.f)
	{
		pos_y -= dock_speed * graphics::getDeltaTime() / 10.0f;
		offset = dock_speed * graphics::getDeltaTime() / 10.0f;
		setVarY(getVarY() - offset);
		setEndY(getVarY());
		onetime = true;

		if (indock)
		{
			graphics::playSound(std::string(ASSET_PATH) + "minimize.wav", 0.7f, 0);

		}
		indock = false;
	}
}

void Dock::draw()
{
	//draw dock
	graphics::Brush br;
	br.outline_opacity = 0.0f;
	br.texture = std::string(ASSET_PATH) + "dock.png";
	graphics::drawRect(pos_x, pos_y, DOCK_WIDTH, DOCK_HEIGHT, br);

	//draw texts
	graphics::Brush br4;
	br4.fill_color[0] = 1.0f;
	br4.fill_color[1] = 1.0f;
	br4.fill_color[2] = 1.0f;

	if (getPosY() >= 70.f)
	{
		graphics::drawText(220.f, 20.f, 18.f, "Genre:", br4);
		graphics::drawText(470.f, 120.f, 18.f, "Filter by:", br4);
		graphics::drawText(220.f, 120.f, 18.f, "Year:", br4);
		graphics::drawText(470.f, 145.f, 18.f, "Title:", br4);
		graphics::drawText(220.f, 155.f, 12.f, "From:", br4);
	}
	if (getPosY() >= 120.f)
	{
		graphics::drawText(470.f, 170.f, 18.f, "Actor:", br4);
		graphics::drawText(220.f, 192.f, 12.f, "To:", br4);
		graphics::drawText(470.f, 195.f, 18.f, "Director:", br4);
	}
}

void Dock::init()
{
}
