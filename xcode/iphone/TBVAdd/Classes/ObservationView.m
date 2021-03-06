//
//  ObservationView.m
//  TBVAdd
//
//  Created by Daniel Lauzon on 28/10/08.
//  Copyright 2008 __MyCompanyName__. All rights reserved.
//

#import "ObservationView.h"
#import "DateUtil.h"


@implementation ObservationView

// Define LabelTags:
#define DAY_LBL_TAG 42
#define MONTH_LBL_TAG 43
#define TIME_LBL_TAG 44
#define OBS_LBL_TAG 45
// Column Layout Geometry
#define MARGIN 20
#define MONTH_COL_WIDTH 35
#define TIME_COL_WIDTH 100
#define OBS_COL_WIDTH 100

@synthesize observation;

- (id)initWithFrame:(CGRect)frame {
    if (self = [super initWithFrame:frame]) {
        // Initialization code
		self.opaque = YES;
		self.backgroundColor = [UIColor whiteColor];
        UIColor *sharedGreenTextColor = [UIColor colorWithRed:0.0 green:1.0/3.0 blue:0.0 alpha:1.0];
        
        CGRect lblFrame;
        UILabel *lbl;
        UIFont *font;
        //Day Label
        lblFrame = CGRectMake(MARGIN, 0.0, MONTH_COL_WIDTH, self.bounds.size.height*2/3);
		lbl= [[UILabel alloc] initWithFrame:lblFrame];
        lbl.tag = DAY_LBL_TAG;
        font = [UIFont systemFontOfSize:20];
        lbl.font = font;
        lbl.textColor = sharedGreenTextColor;
        lbl.backgroundColor = [UIColor clearColor];
        lbl.textAlignment = UITextAlignmentCenter;
		[self addSubview:lbl];
        [lbl release];

        //Month Label
        lblFrame = CGRectMake(MARGIN, self.bounds.size.height/2, MONTH_COL_WIDTH, self.bounds.size.height/2-5);
		lbl= [[UILabel alloc] initWithFrame:lblFrame];
        lbl.tag = MONTH_LBL_TAG;
        font = [UIFont systemFontOfSize:10];
        lbl.font = font;
        lbl.textColor = [UIColor darkGrayColor];
        lbl.backgroundColor = [UIColor clearColor];
        lbl.textAlignment = UITextAlignmentCenter;
		[self addSubview:lbl];
        [lbl release];

        //Time Label
        lblFrame = CGRectMake(MARGIN+MONTH_COL_WIDTH+MARGIN, 0, TIME_COL_WIDTH, self.bounds.size.height-5);
		lbl= [[UILabel alloc] initWithFrame:lblFrame];
        lbl.tag = TIME_LBL_TAG;
        font = [UIFont systemFontOfSize:18];
        lbl.font = font;
        lbl.textColor = sharedGreenTextColor;
        //lbl.backgroundColor = [UIColor colorWithWhite:.5 alpha:0.5];
        //lbl.textAlignment = UITextAlignmentLeft;
		[self addSubview:lbl];
        [lbl release];
        
        // Observation Label (weight)
        lblFrame = CGRectMake(self.bounds.size.width-100-MARGIN, 0, OBS_COL_WIDTH, self.bounds.size.height-5);
		lbl= [[UILabel alloc] initWithFrame:lblFrame];
        font = [UIFont boldSystemFontOfSize:20];
        lbl.font = font;
        lbl.tag = OBS_LBL_TAG;
        lbl.backgroundColor = [UIColor clearColor];
        lbl.textAlignment = UITextAlignmentRight;
		lbl.autoresizingMask = UIViewAutoresizingFlexibleLeftMargin;
		[self addSubview:lbl];
        [lbl release];
    }
    return self;
}

- (void)setObservation:(Observation *)newObservation {
	// If the time zone wrapper changes, update the date formatter and abbreviation string.
	if (observation != newObservation) {
		[observation release];
		observation = [newObservation retain];
        
		UILabel *label;
		
		// Day
		label = (UILabel *)[self viewWithTag:DAY_LBL_TAG];
		label.text =[DateUtil formatDate:observation.stamp withFormat:iMDateFormatDayOfMonth];
		// Month
		label = (UILabel *)[self viewWithTag:MONTH_LBL_TAG];
		label.text = [[DateUtil formatDate:observation.stamp withFormat:iMDateFormatShortMonth] uppercaseString];
        // Time 
		label = (UILabel *)[self viewWithTag:TIME_LBL_TAG];
		label.text = [DateUtil formatDate:observation.stamp withFormat:iMDateFormatHM24];

		// Observation
		double d = observation.value / 1000.0;
		label = (UILabel *)[self viewWithTag:OBS_LBL_TAG];
		label.text = [[NSString alloc] initWithFormat:@"%.1f", d];
	}
	// May be the same wrapper, but the date may have changed, so mark for redisplay
	[self setNeedsDisplay];
}


- (void)dealloc {
    [observation release];
    [super dealloc];
}


@end
