require_relative '../lib/modules/CaseClassModule.rb'
require_relative '../lib/CaseClass.rb'

class Padre
  def metodo_padre
    "SOY EL PADRE"
  end

  def to_s
    'viene del padre'
  end
end

module M
  def metodo_m
    "SOY DEL MIXIN M"
  end
end

case_class CC_A < Padre do
  include M
  def metodo_hijo
    "SOY EL HIJO A"
  end
end

case_class CC_B do

  attr_accessor :nombre, :apellido

  include M
  def metodo_hijo
    "SOY EL HIJO B"
  end

  def to_s
    'Mi to_s'
  end

end

case_class Alumno do
  attr_accessor :nombre, :nota
end
case_class Curso do
  attr_accessor :materia, :codigo, :alumnos
end
