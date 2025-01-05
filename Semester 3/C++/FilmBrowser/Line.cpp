#include "graphics.h"
#include "Line.h"

void LineFrom::update()
{
	graphics::MouseState ms;
	graphics::getMouseState(ms);

	float mx = graphics::windowToCanvasX(ms.cur_pos_x);
	float my = graphics::windowToCanvasY(ms.cur_pos_y);
	
	//when dragginng slowly change yearfrom & set the rect on the coordinates of the mouse
	if (ms.dragging && widget_highlighted)
	{
		if (mx >= 250.f && mx <= 400.f)
		{
			//prevmx checks the direction of the movement(right or left)
			prevmx = mx - getPosX();
			setPosX(mx);
			setStartX(getPosX() - 4.f);
			setEndX(getPosX() + 4.f);
			if (fmodf(getPosX(), 1.f) == 0 || fmodf(getPosX(), 2.f) == 0 || fmodf(getPosX(), 3.f) == 0 || fmodf(getPosX(), 5.f) == 0)
			{  
				if (prevmx < 0 && yearfrom >= 1973)
				{
					yearfrom--;
				}
				else if (prevmx > 0 && yearfrom <= 2007)
				{
					yearfrom++;
				}
			}
		}
	}
}

void LineFrom::draw()
{
	//draw only when dock is highlighted
	if (dock_highlighted)
	{
		//draw line
		graphics::Brush br2;
		br2.fill_color[0] = 1.0f;
		br2.fill_color[1] = 1.0f;
		br2.fill_color[2] = 1.0f;
		graphics::drawText(getPosX() - 10.f, getPosY() - 13.f, 10.f, std::to_string(yearfrom), br2);
		graphics::drawLine(getPosX1(), getPosY1(), getPosX2(), getPosY2(), br2);

		//draw rect
		graphics::Brush br;
		br.fill_color[0] = 0.0f;
		br.fill_color[1] = 0.0f;
		br.fill_color[2] = 0.0f;
		br.outline_opacity = 1.0f;
		graphics::drawRect(getPosX(), getPosY(), 8.f, 12.f, br);
	}
}

void LineFrom::init()
{
}

void LineTo::update()
{
	graphics::MouseState ms;
	graphics::getMouseState(ms);

	float mx = graphics::windowToCanvasX(ms.cur_pos_x);
	float my = graphics::windowToCanvasY(ms.cur_pos_y);

	//when dragginng slowly change yearto & set the rect on the coordinates of the mouse
	if (ms.dragging && widget_highlighted)
	{
		if (mx >= 250.f && mx <= 400.f)
		{
			//prevmx checks the direction of the movement(right or left)
			float prevmx = mx - getPosX();
			setPosX(mx);
			setStartX(getPosX() - 4.f);
			setEndX(getPosX() + 4.f);
			if (fmodf(getPosX(), 1.f) == 0 || fmodf(getPosX(), 2.f) == 0 || fmodf(getPosX(), 3.f) == 0 || fmodf(getPosX(), 5.f) == 0)
			{		
				if (prevmx < 0 && yearto >= 1972)
				{
					yearto--;
				}
				else if (prevmx > 0 && yearto <= 2007)
				{
					yearto++;
				}
			}
		}
	}
}

void LineTo::draw()
{
	//draw only when dock is highlighted
	if (dock_highlighted)
	{
		//draw line
		graphics::Brush br1;
		br1.fill_color[0] = 1.0f;
		br1.fill_color[1] = 1.0f;
		br1.fill_color[2] = 1.0f;
		graphics::drawText(getPosX() - 10.f, getPosY() - 13.f, 10.f, std::to_string(yearto), br1);
		graphics::drawLine(getPosX1(), getPosY1(), getPosX2(), getPosY2(), br1);

		//draw rect
		graphics::Brush br;
		br.fill_color[0] = 0.0f;
		br.fill_color[1] = 0.0f;
		br.fill_color[2] = 0.0f;
		br.outline_opacity = 1.0f;
		graphics::drawRect(getPosX(), getPosY(), 8.f, 12.f, br);
	}
}

void LineTo::init()
{
}
