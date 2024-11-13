import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BackgroundVideoComponent } from './background-video.component';

describe('BackgroundVideoComponent', () => {
  let component: BackgroundVideoComponent;
  let fixture: ComponentFixture<BackgroundVideoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BackgroundVideoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BackgroundVideoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
