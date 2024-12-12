import { ComponentFixture, TestBed } from '@angular/core/testing';

import { D3GraphAnalysisComponent } from './d3-graph-analysis.component';

describe('D3GraphAnalysisComponent', () => {
  let component: D3GraphAnalysisComponent;
  let fixture: ComponentFixture<D3GraphAnalysisComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [D3GraphAnalysisComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(D3GraphAnalysisComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
