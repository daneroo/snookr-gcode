//
//  AddObservationViewController.m
//  Weightrical
//
//  Created by Daniel Lauzon on 09/10/08.
//  Copyright 2008 __MyCompanyName__. All rights reserved.
//

#import "AddObservationViewController.h"
#import "DateUtil.h"

@implementation AddObservationViewController
@synthesize datePicker;
@synthesize weightPicker;
@synthesize nowLabel;
@synthesize changeDateButton;
@synthesize delegate;

- (NSInteger)selectedValue {
    NSInteger val = 1000*[weightPicker selectedRowInComponent:0] + 100*[weightPicker selectedRowInComponent:2];
    return val;
}
- (NSDate *)selectedDate {
    return datePicker.date;
}

- (void)setInitialWeight:(NSInteger)weight {
    NSInteger intPart = weight/1000;
    NSInteger digit1 = (weight%1000)/100;
    [weightPicker selectRow:intPart inComponent:0 animated:NO];
    [weightPicker selectRow: digit1 inComponent:2 animated:NO];
}

- (IBAction) makeDatePickerVisible:(id) sender {
    datePicker.hidden = NO;
    nowLabel.hidden = YES;
    changeDateButton.hidden = YES;
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
	if (self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil]) {
		self.title = @"Add Observation";
        
        UIBarButtonItem *saveButtonItem = [[[UIBarButtonItem alloc]
                                            initWithBarButtonSystemItem: UIBarButtonSystemItemSave
                                            target:self action:@selector(save)] autorelease];
        self.navigationItem.rightBarButtonItem = saveButtonItem;
        
        UIBarButtonItem *cancelButtonItem = [[[UIBarButtonItem alloc]
                                              initWithBarButtonSystemItem: UIBarButtonSystemItemCancel
                                              target:self action:@selector(cancel)] autorelease];
        
        self.navigationItem.leftBarButtonItem = cancelButtonItem;
        
    }
	return self;
}

- (void)viewWillAppear:(BOOL)animated {
    [datePicker setDate:[NSDate date]];

	// This was moved from RootViewController::popupAddObservationModal to here
	// because the weightpicker was not yet ready so --setInitialWeight
	Observation *obs = [delegate getLatestObservation];
	if (obs) {
		NSLog(@"Get Latest Obs val: %ld",obs.value);
		[self setInitialWeight:obs.value];
    } else {
        [self setInitialWeight:100000];
    }
	
	
    // Max Date makes UI confusing, maybe a warning (future date) would be better
    //datePicker.maximumDate = datePicker.date;
    
    nowLabel.text = [DateUtil formatDate:datePicker.date withFormat:iMDateFormatFullISO];
}

- (void)save  {
    //NSLog(@"Hello from save callback");
	[self.delegate addAndSaveObservation:[self selectedValue] withStamp:[self selectedDate]];
	[self dismissModalViewControllerAnimated:YES];
}

- (void)cancel  {
    //NSLog(@"Hello from cancel callback");
	[self dismissModalViewControllerAnimated:YES];
}


/*
 // Implement loadView to create a view hierarchy programmatically.
 - (void)loadView {
 }
 */

/*
 // Implement viewDidLoad to do additional setup after loading the view.
 - (void)viewDidLoad {
 [super viewDidLoad];
 }
 */


- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning]; // Releases the view if it doesn't have a superview
    // Release anything that's not essential, such as cached data
}


- (void)dealloc {
    [super dealloc];
}


#pragma mark -
#pragma mark PickerView delegate methods

/* pickerView selection callback example
- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component
{
    double val = [pickerView selectedRowInComponent:0] + 0.1l* [pickerView selectedRowInComponent:2];
    
    NSNumber *num = [NSNumber numberWithDouble:val];
    NSLog(@"Picked %d : %d value: %@", component,row,[num stringValue]);
}
*/


- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component
{
    //NSLog(@"Picker titleForRow : %d comp: %d",row,component);
    if (component == 1) {
        return @".";
	}
    return [[NSNumber numberWithInt:row] stringValue];
}

- (CGFloat)pickerView:(UIPickerView *)pickerView widthForComponent:(NSInteger)component
{
	if (component == 0) return 60.0;	// hold thre digits
	if (component == 1) return 20.0;    // hold the decimal point
	if (component == 2) return 40.0;    // hold the decimal digit
	return 40; // whatever
}

- (CGFloat)NOTpickerView:(UIPickerView *)pickerView rowHeightForComponent:(NSInteger)component
{
	return 40.0;
}

- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component
{
    //NSLog(@"Picker numberOfRowsInComp: %d",component);
    switch (component) {
        case 0:
            return 400;
            break;
        case 2:
            return 10;
            break;
        default:
            return 1;
            break;
    }
}

- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
    //NSLog(@"Picker numberOfComps: delegate:%d instance var:%d",pickerView,weightPicker);
	return 3;
}



@end
