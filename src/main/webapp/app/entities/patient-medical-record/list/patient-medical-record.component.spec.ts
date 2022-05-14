import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PatientMedicalRecordService } from '../service/patient-medical-record.service';

import { PatientMedicalRecordComponent } from './patient-medical-record.component';

describe('PatientMedicalRecord Management Component', () => {
  let comp: PatientMedicalRecordComponent;
  let fixture: ComponentFixture<PatientMedicalRecordComponent>;
  let service: PatientMedicalRecordService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PatientMedicalRecordComponent],
    })
      .overrideTemplate(PatientMedicalRecordComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PatientMedicalRecordComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PatientMedicalRecordService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 'ABC' }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.patientMedicalRecords?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });
});
