require 'test/unit'
require_relative '../lib/modules/SintaxModule.rb'

class CaseClassTest < Test::Unit::TestCase

  def setup

    case_class Alumno do
      attr_accessor :nombre, :nota
    end
    case_class Curso do
      attr_accessor :materia, :codigo, :alumnos
    end

  end

  # Funcion constructora
  def test_funcion_constructora
    alumno = Alumno('Nico', 8)
    assert_equal alumno.class, Alumno
  end

  # Tests to_s
  def test_to_s
    alumno = Alumno.new('Fede',6)    
    assert_equal alumno.to_s, 'Alumno(Fede, 6)'
  end
  
  #def test_to_s_with_array
  #  assert_equal @curso_tadp.to_s, 'Curso(TADP, K3031, [Alumno(Nico, 8), Alumno(Miguel, 2)])'
  #end

end